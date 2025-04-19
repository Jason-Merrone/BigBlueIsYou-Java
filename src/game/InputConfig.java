package game;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;

public class InputConfig {
    public enum Action {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        UNDO,
        RESET,
    }

    private Map<Action, Integer> actionKey = new HashMap<Action, Integer>();

    public InputConfig() {
        rebind(Action.UP, GLFW_KEY_W);
        rebind(Action.DOWN, GLFW_KEY_S);
        rebind(Action.LEFT, GLFW_KEY_A);
        rebind(Action.RIGHT, GLFW_KEY_D);
        rebind(Action.UNDO, GLFW_KEY_Z);
        rebind(Action.RESET, GLFW_KEY_R);
    }

    public void rebind(Action action, int newKey) {
        actionKey.put(action, newKey);
    }

    public int getActionKey(Action action) {
        return actionKey.get(action);
    }

    private static final String CONFIG_FILE = "input_config.json";

    /**
     * Write current bindings to CONFIG_FILE.
     * FileWriter auto‑creates or overwrites the file.
     */
    public void save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load bindings from CONFIG_FILE.
     * If the file doesn’t exist, write out defaults first.
     */
    public void load() {
        File file = new File(CONFIG_FILE);
        if (!file.exists()) {
            // no config yet → write default file
            save();
            return;
        }

        Gson gson = new Gson();
        try (FileReader reader = new FileReader(file)) {
            InputConfig loaded = gson.fromJson(reader, InputConfig.class);
            this.actionKey = loaded.actionKey;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
