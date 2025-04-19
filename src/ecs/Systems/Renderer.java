package ecs.Systems;

import ecs.Components.Noun;
import ecs.Components.Object;
import ecs.Components.Position;
import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Rectangle;
import edu.usu.graphics.Texture;
import org.joml.Vector2f;

import java.util.HashMap;
import java.util.Map;

public class Renderer extends System {
    private final Graphics2D graphics;
    private final int GRID_WIDTH;
    private final int GRID_HEIGHT;
    private final float CELL_WIDTH;
    private final float CELL_HEIGHT;
    private final float OFFSET_X = 0.1f;
    private final float OFFSET_Y = 0.1f;

    private static final float ANIMATION_SLOWDOWN = 4.0f;
    private static final float BASE_FRAME_TIME   = 0.04f * ANIMATION_SLOWDOWN;

    private static class CachedSpriteInfo {
        final Sprite sprite;
        final String location;

        CachedSpriteInfo(Sprite sprite, String location) {
            this.sprite = sprite;
            this.location = location;
        }
    }

    private final Map<ecs.Entities.Entity, CachedSpriteInfo> spriteCache = new HashMap<>();

    public Renderer(Graphics2D graphics, int gridWidth, int gridHeight) {
        super(ecs.Components.Render.class, ecs.Components.Sprite.class, ecs.Components.Position.class);
        this.graphics   = graphics;
        this.GRID_WIDTH = gridWidth;
        this.GRID_HEIGHT= gridHeight;
        CELL_WIDTH      = (1.0f - OFFSET_X * 2) / GRID_WIDTH;
        CELL_HEIGHT     = (1.0f - OFFSET_Y * 2) / GRID_HEIGHT;
    }

    @Override
    public void update(double elapsedTime) {
        Rectangle bg = new Rectangle(
                -0.5f + OFFSET_X,
                -0.5f + OFFSET_Y,
                GRID_WIDTH * CELL_WIDTH,
                GRID_HEIGHT * CELL_HEIGHT
        );
        graphics.draw(bg, Color.DARK_GRAY);

        for (var entity : entities.values()) {
            if (!entity.contains(ecs.Components.objectattributes.You.class)
                    && (entity.contains(ecs.Components.objecttypes.Floor.class)
                    || entity.contains(ecs.Components.objecttypes.Water.class)
                    || entity.contains(ecs.Components.objecttypes.Lava.class)
                    || entity.contains(ecs.Components.objecttypes.Grass.class)
                    || entity.contains(ecs.Components.objecttypes.Flowers.class))) {
                renderEntity(entity, elapsedTime);
            }
        }

        for (var entity : entities.values()) {
            if (!entity.contains(ecs.Components.objectattributes.You.class)
                    && !(entity.contains(ecs.Components.objecttypes.Floor.class)
                    || entity.contains(ecs.Components.objecttypes.Water.class)
                    || entity.contains(ecs.Components.objecttypes.Lava.class)
                    || entity.contains(ecs.Components.objecttypes.Grass.class)
                    || entity.contains(ecs.Components.objecttypes.Flowers.class))) {
                renderEntity(entity, elapsedTime);
            }
        }

        for (var entity : entities.values()) {
            if (entity.contains(ecs.Components.objectattributes.You.class)) {
                renderEntity(entity, elapsedTime);
            }
        }
    }

    private void renderEntity(ecs.Entities.Entity entity, double dt) {
        var spriteComp = entity.get(ecs.Components.Sprite.class);
        var pos        = entity.get(ecs.Components.Position.class);
        if (spriteComp == null || pos == null) return;

        String currentLocation = spriteComp.location;
        CachedSpriteInfo cachedInfo = spriteCache.get(entity);
        Sprite anim;

        if (cachedInfo != null && cachedInfo.location.equals(currentLocation)) {
            anim = cachedInfo.sprite;
        } else {
            if (currentLocation == null || currentLocation.trim().isEmpty()) return;
            boolean isStatic = entity.contains(Object.class)
                    && entity.get(Object.class).type == ecs.Entities.Object.ObjectType.BIGBLUE;
            Texture sheet = new Texture(currentLocation);
            float[] timings = isStatic
                    ? new float[]{ Float.POSITIVE_INFINITY }
                    : new float[]{ BASE_FRAME_TIME, BASE_FRAME_TIME, BASE_FRAME_TIME };
            Vector2f size = new Vector2f(CELL_WIDTH, CELL_HEIGHT);
            anim = new Sprite(sheet, timings, size, new Vector2f());
            spriteCache.put(entity, new CachedSpriteInfo(anim, currentLocation));
        }

        float cx = -0.5f + OFFSET_X + pos.getX() * CELL_WIDTH + CELL_WIDTH * 0.5f;
        float cy = -0.5f + OFFSET_Y + pos.getY() * CELL_HEIGHT + CELL_HEIGHT * 0.5f;
        anim.setCenter(cx, cy);
        anim.update(dt);
        anim.draw(graphics, Color.WHITE);
    }
}
