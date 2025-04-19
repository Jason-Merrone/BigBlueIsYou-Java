// KeyboardInput.java
package ecs.Systems;

import static org.lwjgl.glfw.GLFW.*;
import ecs.Entities.Entity;

public class KeyboardInput extends System {
    private final long   window;
    private final Movement movement;
    private       Undo    undo;
    private Sentence sentence;

    private boolean wasW, wasA, wasS, wasD, wasZ;

    public KeyboardInput(long window, Movement movement, Undo undo, Sentence sentence) {
        super();
        this.window   = window;
        this.movement = movement;
        this.undo     = undo;
        this.sentence = sentence;
    }

    public void setUndo(Undo undo) {
        this.undo = undo;
    }

    @Override
    public void update(double dt) {
        boolean w = glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS;
        boolean a = glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS;
        boolean s = glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS;
        boolean d = glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS;
        boolean z = glfwGetKey(window, GLFW_KEY_Z) == GLFW_PRESS;

        if (z && !wasZ) {
            undo.pop();
            sentence.update(dt);
        } else if (w && !wasW) {
            undo.push();
            movement.updateDirection(Movement.Direction.UP);
            movement.update(dt);
            sentence.update(dt);
        } else if (a && !wasA) {
            undo.push();
            movement.updateDirection(Movement.Direction.LEFT);
            movement.update(dt);
            sentence.update(dt);
        } else if (s && !wasS) {
            undo.push();
            movement.updateDirection(Movement.Direction.DOWN);
            movement.update(dt);
            sentence.update(dt);
        } else if (d && !wasD) {
            undo.push();
            movement.updateDirection(Movement.Direction.RIGHT);
            movement.update(dt);
            sentence.update(dt);
        }

        wasW = w;
        wasA = a;
        wasS = s;
        wasD = d;
        wasZ = z;
    }
}
