package ecs.Systems;

import edu.usu.graphics.AnimatedSprite;
import edu.usu.graphics.Texture;
import org.joml.Vector2f;

/* Thin wrapper so Renderer can change the centre each frame. */
public class Sprite extends AnimatedSprite {

    public Sprite(Texture sheet, float[] frameTimes,
                  Vector2f size, Vector2f center) {
        super(sheet, frameTimes, size, center);
    }

    /** Expose centre mutator while keeping everything else unchanged. */
    public void setCenter(float x, float y) {
        this.center.set(x, y);
    }
}
