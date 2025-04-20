// KeyboardInput.java
package ecs.Systems;

import static org.lwjgl.glfw.GLFW.*;
import ecs.Entities.Entity;
import game.InputConfig;

public class KeyboardInput extends System {
    private final long   window;
    private final Movement movement;
    private       Undo    undo;
    private Sentence sentence;
    private InputConfig inputConfig;

    private boolean wasW, wasA, wasS, wasD, wasZ;

    public KeyboardInput(long window, Movement movement, Undo undo, Sentence sentence, InputConfig inputConfig) {
        super();
        this.window   = window;
        this.movement = movement;
        this.undo     = undo;
        this.sentence = sentence;
        this.inputConfig = inputConfig;
    }

    public void setUndo(Undo undo) {
        this.undo = undo;
    }

    @Override
    public void update(double dt) {
        boolean w = glfwGetKey(window, inputConfig.getActionKey(InputConfig.Action.UP)) == GLFW_PRESS;
        boolean a = glfwGetKey(window, inputConfig.getActionKey(InputConfig.Action.LEFT)) == GLFW_PRESS;
        boolean s = glfwGetKey(window, inputConfig.getActionKey(InputConfig.Action.DOWN)) == GLFW_PRESS;
        boolean d = glfwGetKey(window, inputConfig.getActionKey(InputConfig.Action.RIGHT)) == GLFW_PRESS;
        boolean z = glfwGetKey(window, inputConfig.getActionKey(InputConfig.Action.UNDO)) == GLFW_PRESS;

        if (z && !wasZ) {
            undo.pop();
            sentence.update(dt);
        } else if (w && !wasW) {
            movement.updateDirection(Movement.Direction.UP);
            movement.update(dt);
            sentence.update(dt);
        } else if (a && !wasA) {
            movement.updateDirection(Movement.Direction.LEFT);
            movement.update(dt);
            sentence.update(dt);
        } else if (s && !wasS) {
            movement.updateDirection(Movement.Direction.DOWN);
            movement.update(dt);
            sentence.update(dt);
        } else if (d && !wasD) {
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
