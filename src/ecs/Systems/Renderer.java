package ecs.Systems;

import ecs.Components.Noun;
import ecs.Components.Object;
import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Rectangle;
import edu.usu.graphics.Texture;
import org.joml.Vector2f;

import java.util.HashMap;
import java.util.Map;

public class Renderer extends System {

    private final Graphics2D graphics;

    private final int   GRID_WIDTH;
    private final int   GRID_HEIGHT;
    private final float CELL_WIDTH;
    private final float CELL_HEIGHT;
    private final float OFFSET_X = 0.1f;
    private final float OFFSET_Y = 0.1f;

    /* cache one animated sprite per entity */
    private final Map<ecs.Entities.Entity, Sprite> spriteCache = new HashMap<>();

    public Renderer(Graphics2D graphics, int gridWidth, int gridHeight) {
        super(ecs.Components.Render.class);
        this.graphics    = graphics;
        this.GRID_WIDTH  = gridWidth;
        this.GRID_HEIGHT = gridHeight;

        CELL_WIDTH  = (1.0f - OFFSET_X * 2) / GRID_WIDTH;
        CELL_HEIGHT = (1.0f - OFFSET_Y * 2) / GRID_HEIGHT;
    }

    @Override
    public void update(double elapsedTime) {
        // background
        Rectangle bg = new Rectangle(
                -0.5f + OFFSET_X, -0.5f + OFFSET_Y,
                GRID_WIDTH * CELL_WIDTH, GRID_HEIGHT * CELL_HEIGHT);
        graphics.draw(bg, Color.BLUE);

        // entities
        for (var entity : entities.values()) {
            renderEntity(entity, elapsedTime);
        }
    }

    private void renderEntity(ecs.Entities.Entity entity, double dt) {
        var spriteComp = entity.get(ecs.Components.Sprite.class);
        var pos        = entity.get(ecs.Components.Position.class);

        boolean isStatic;

        isStatic = entity.contains(Object.class) && entity.get(Object.class).type == ecs.Entities.Object.ObjectType.BIGBLUE;

        /* build (once) or fetch the AnimatedSprite */
        Sprite anim = spriteCache.computeIfAbsent(entity, e -> {
            Texture sheet     = new Texture(spriteComp.location);
            float[] timings = isStatic
                    ? new float[]{ Float.POSITIVE_INFINITY }
                    : new float[]{ 0.04f, 0.04f, 0.04f };
            Vector2f size     = new Vector2f(CELL_WIDTH, CELL_HEIGHT);
            return new Sprite(sheet, timings, size, new Vector2f());
        });

        /* update sprite centre to this entity’s grid cell */
        float cx = -0.5f + OFFSET_X + pos.getX() * CELL_WIDTH  + CELL_WIDTH  * 0.5f;
        float cy = -0.5f + OFFSET_Y + pos.getY() * CELL_HEIGHT + CELL_HEIGHT * 0.5f;
        anim.setCenter(cx, cy);

        /* progress animation and draw */
        anim.update(dt);
        anim.draw(graphics, Color.WHITE);
    }
}
