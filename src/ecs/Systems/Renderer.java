package ecs.Systems;

import ecs.Components.Noun; // Assuming Noun might be needed indirectly, keep imports clean
import ecs.Components.Object;
import ecs.Components.Position; // Need Position component
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

    // --- Helper class to store cached sprite and its source location ---
    private static class CachedSpriteInfo {
        final Sprite sprite;
        final String location; // Store the location used to create this sprite

        CachedSpriteInfo(Sprite sprite, String location) {
            this.sprite = sprite;
            this.location = location;
        }
    }
    // --- Cache now stores CachedSpriteInfo objects ---
    private final Map<ecs.Entities.Entity, CachedSpriteInfo> spriteCache = new HashMap<>();


    public Renderer(Graphics2D graphics, int gridWidth, int gridHeight) {
        super(ecs.Components.Render.class, ecs.Components.Sprite.class, ecs.Components.Position.class); // Ensure Renderer requires necessary components
        this.graphics = graphics;
        this.GRID_WIDTH = gridWidth;
        this.GRID_HEIGHT = gridHeight;
        CELL_WIDTH = (1.0f - OFFSET_X * 2) / GRID_WIDTH;
        CELL_HEIGHT = (1.0f - OFFSET_Y * 2) / GRID_HEIGHT;
    }

    @Override
    public void update(double elapsedTime) {

        Rectangle bg = new Rectangle(
                -0.5f + OFFSET_X,
                -0.5f + OFFSET_Y,
                GRID_WIDTH * CELL_WIDTH,
                GRID_HEIGHT * CELL_HEIGHT);
        graphics.draw(bg, Color.BLUE);

        for (var entity : entities.values()) {
            renderEntity(entity, elapsedTime);
        }
    }

    private void renderEntity(ecs.Entities.Entity entity, double dt) {
        var spriteComp = entity.get(ecs.Components.Sprite.class);
        var pos = entity.get(ecs.Components.Position.class);

        if (spriteComp == null || pos == null) {
            return;
        }

        String currentLocation = spriteComp.location;
        CachedSpriteInfo cachedInfo = spriteCache.get(entity);
        Sprite anim;

        if (cachedInfo != null && cachedInfo.location != null && cachedInfo.location.equals(currentLocation)) {

            anim = cachedInfo.sprite;
        } else {
            // Check if location is valid before creating Texture
            if (currentLocation == null || currentLocation.trim().isEmpty()) {
                // java.lang.System.err.println("Entity " + entity.getId() + " has null or empty sprite location.");
                return;
            }

            boolean isStatic = entity.contains(Object.class) && entity.get(Object.class).type == ecs.Entities.Object.ObjectType.BIGBLUE; // Determine if static
            Texture sheet = new Texture(currentLocation); // Load texture from the CURRENT location

            // *** CORRECTED LINE ***
            float[] timings = isStatic ? new float[]{ Float.POSITIVE_INFINITY } : new float[]{ 0.04f, 0.04f, 0.04f }; // Example timings

            Vector2f size = new Vector2f(CELL_WIDTH, CELL_HEIGHT);
            anim = new Sprite(sheet, timings, size, new Vector2f()); // Create the new sprite

            // Update the cache with the new sprite and its location
            spriteCache.put(entity, new CachedSpriteInfo(anim, currentLocation));
        }

        /* update sprite centre to this entity’s grid cell */
        float cx = -0.5f + OFFSET_X + pos.getX() * CELL_WIDTH + CELL_WIDTH * 0.5f;
        float cy = -0.5f + OFFSET_Y + pos.getY() * CELL_HEIGHT + CELL_HEIGHT * 0.5f;
        anim.setCenter(cx, cy);

        /* progress animation and draw */
        anim.update(dt);
        anim.draw(graphics, Color.WHITE);
    }
}