package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class KeyboardInput {

    // The type of method to invoke when a keyboard event is invoked
    public interface ICommand {
        void invoke(double elapsedTime);
    }

    private final long window;
    // registered callbacks by key
    private final HashMap<Integer, CommandEntry> commandEntries = new HashMap<>();
    // previous pressed state for each key
    private final HashMap<Integer, Boolean> keysPressed = new HashMap<>();

    // For global key tracking
    private final HashMap<Integer, Boolean> globalKeysPressed = new HashMap<>();
    private final List<Integer> newPressedKeys = new ArrayList<>();

    public KeyboardInput(long window) {
        this.window = window;
    }

    public void registerCommand(int key, boolean keyPressOnly, ICommand callback) {
        // seed initial pressed state from hardware so held keys are ignored until release
        boolean currentlyDown = (glfwGetKey(window, key) == GLFW_PRESS);
        keysPressed.put(key, currentlyDown);

        // now register the command
        commandEntries.put(key, new CommandEntry(key, keyPressOnly, callback));
    }

    public void update(double elapsedTime) {
        for (var entry : commandEntries.entrySet()) {
            int key = entry.getKey();
            CommandEntry ce = entry.getValue();

            if (ce.keyPressOnly) {
                // only invoke if key transitioned from up→down
                if (isKeyNewlyPressed(key)) {
                    ce.callback.invoke(elapsedTime);
                }
            } else {
                // invoke continuously while key is down
                if (glfwGetKey(window, key) == GLFW_PRESS) {
                    ce.callback.invoke(elapsedTime);
                }
            }

            // update state for next frame
            keysPressed.put(key, glfwGetKey(window, key) == GLFW_PRESS);
        }
    }

    private boolean isKeyNewlyPressed(int key) {
        // return true if currently pressed but wasn't pressed last frame
        return (glfwGetKey(window, key) == GLFW_PRESS)
                && !keysPressed.getOrDefault(key, false);
    }

    public void updateGlobalKeys() {
        newPressedKeys.clear();
        // scan all keys for newly pressed
        for (int key = 32; key <= GLFW_KEY_LAST; key++) {
            boolean pressed = (glfwGetKey(window, key) == GLFW_PRESS);
            boolean wasPressed = globalKeysPressed.getOrDefault(key, false);
            if (pressed && !wasPressed) {
                newPressedKeys.add(key);
            }
            globalKeysPressed.put(key, pressed);
        }
    }

    public List<Integer> getNewlyPressedKeys() {
        // return a copy so external code can't modify internal list
        return new ArrayList<>(newPressedKeys);
    }

    // internal struct to hold command details
    private record CommandEntry(int key, boolean keyPressOnly, ICommand callback) {}
}