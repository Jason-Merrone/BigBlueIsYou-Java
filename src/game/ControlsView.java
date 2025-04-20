package game;

import edu.usu.audio.SoundManager;
import edu.usu.graphics.Color;
import edu.usu.graphics.Font;
import edu.usu.graphics.Graphics2D;

import static org.lwjgl.glfw.GLFW.*;

import java.util.List;

public class ControlsView extends GameStateView {
    private KeyboardInput inputKeyboard;

    private int currentSelection = 0;
    private boolean editing = false;
    private GameStateEnum nextGameState = GameStateEnum.Controls;

    private Font fontMenu;
    private Font fontSelected;
    private Font fontEditing;

    @Override
    public void initialize(Graphics2D graphics, LevelReader gameLevels, InputConfig inputConfig, SoundManager audio) {
        super.initialize(graphics, gameLevels, inputConfig, audio);

        // load fonts
        fontMenu     = new Font("resources/fonts/Gaegu-Regular.ttf", 36, false);
        fontSelected = new Font("resources/fonts/Gaegu-Bold.ttf",    36, false);
        fontEditing  = new Font("resources/fonts/Gaegu-Bold.ttf",    36, false);
    }

    private void initializeKeys() {
        inputKeyboard = new KeyboardInput(graphics.getWindow());

        // navigate up/down through the list (only when not editing)
        inputKeyboard.registerCommand(inputConfig.getActionKey(InputConfig.Action.UP), true, dt -> {
            if (!editing) {
                int n = InputConfig.Action.values().length;
                currentSelection = (currentSelection - 1 + n) % n;
            }
        });
        inputKeyboard.registerCommand(inputConfig.getActionKey(InputConfig.Action.DOWN), true, dt -> {
            if (!editing) {
                int n = InputConfig.Action.values().length;
                currentSelection = (currentSelection + 1) % n;
            }
        });

        // enter edit mode
        inputKeyboard.registerCommand(GLFW_KEY_ENTER, true, dt -> {
            if (!editing) {
                editing = true;
            }
        });

        // escape: either cancel edit or go back
        inputKeyboard.registerCommand(GLFW_KEY_ESCAPE, true, dt -> {
            if (editing) {
                editing = false;
            } else {
                inputConfig.save();
                nextGameState = GameStateEnum.MainMenu;
            }
        });
    }

    @Override
    public void initializeSession() {
        initializeKeys();
        currentSelection = 0;
        editing = false;
        nextGameState = GameStateEnum.Controls;
    }

    @Override
    public GameStateEnum processInput(double elapsedTime) {
        // update keyboard state
        inputKeyboard.update(elapsedTime);

        // if we're in edit mode, capture the first newly pressed key
        if (editing) {
            List<Integer> newly = inputKeyboard.getNewlyPressedKeys();
            if (!newly.isEmpty()) {
                int newKey = newly.get(0);
                // bind it on the real, global InputConfig
                if (newKey != GLFW_KEY_ESCAPE && newKey != GLFW_KEY_ENTER) {
                    InputConfig.Action action = InputConfig.Action.values()[currentSelection];
                    inputConfig.rebind(action, newKey);
                    initializeKeys();
                    editing = false;
                }
            }
        }

        return nextGameState;
    }

    @Override
    public void update(double elapsedTime) {
        if (editing) {
            inputKeyboard.updateGlobalKeys();
        }
    }

    @Override
    public void render(double elapsedTime) {
        final float ITEM_HEIGHT = 0.07f;
        float top =  -0.2f;

        for (int i = 0; i < InputConfig.Action.values().length; i++) {
            InputConfig.Action action = InputConfig.Action.values()[i];
            boolean isSelected = (i == currentSelection);
            Font font = editing && isSelected
                    ? fontEditing
                    : (isSelected ? fontSelected : fontMenu);
            Color color = editing && isSelected
                    ? Color.LIGHT_BLUE
                    : (isSelected ? Color.WHITE : Color.LIGHT_GRAY);

            // display label and current key
            String label = switch (action) {
                case UP    -> "Up";
                case DOWN  -> "Down";
                case LEFT  -> "Left";
                case RIGHT -> "Right";
                case UNDO  -> "Undo";
                case RESET -> "Reset";
            };
            int keyCode = inputConfig.getActionKey(action);
            String keyName = validateAndCreateKeyName(keyCode);

            top = renderControlsMenuItem(font, label, keyName, top, ITEM_HEIGHT, color);
        }
    }

    private String validateAndCreateKeyName(int keyCode) {
        String keyName = glfwGetKeyName(keyCode, 0);
        if (keyName == null) {
            if (keyCode == GLFW_KEY_UP) keyName = "Up";
            else if (keyCode == GLFW_KEY_DOWN) keyName = "Down";
            else if (keyCode == GLFW_KEY_LEFT) keyName = "Left";
            else if (keyCode == GLFW_KEY_RIGHT) keyName = "Right";
            else if (keyCode == GLFW_KEY_LEFT_SHIFT) keyName = "Left Shift";
            else if (keyCode == GLFW_KEY_RIGHT_SHIFT) keyName = "Right Shift";
            else if (keyCode == GLFW_KEY_TAB) keyName = "Tab";
            else if (keyCode == GLFW_KEY_CAPS_LOCK) keyName = "Caps Lock";
            else if (keyCode == GLFW_KEY_BACKSPACE) keyName = "Backspace";
            else if (keyCode == GLFW_KEY_LEFT_CONTROL) keyName = "Left Control";
            else if (keyCode == GLFW_KEY_RIGHT_CONTROL) keyName = "Right Control";
            else if (keyCode == GLFW_KEY_LEFT_ALT) keyName = "Left Alt/Option";
            else if (keyCode == GLFW_KEY_RIGHT_ALT) keyName = "Right Alt/Option";
            else if (keyCode == GLFW_KEY_RIGHT_SUPER) keyName = "Right Command";
            else if (keyCode == GLFW_KEY_LEFT_SUPER) keyName = "Left Command";
            else if (keyCode == GLFW_KEY_SPACE) keyName = "Space";
            else keyName = "Undefined";
        } else {
            keyName = keyName.toUpperCase();
        }

        return keyName;
    }

    /**
     * Centers the text horizontally at the given vertical position, then returns
     * the y-position for the next item.
     */
    private float renderControlsMenuItem(Font font, String label, String keyName, float top, float height, Color color) {
        float trueHeight = height * 1.3f;
        float LABEL_ALIGNMENT = -0.2f;
        float COLON_ALIGNMENT = 0.0f;
        float KEY_ALIGNMENT = 0.05f;
        graphics.drawTextByHeight(font, label, LABEL_ALIGNMENT, top, trueHeight, color);
        graphics.drawTextByHeight(font, ":", COLON_ALIGNMENT, top, trueHeight, color);
        graphics.drawTextByHeight(font, keyName, KEY_ALIGNMENT, top, trueHeight, color);
        return top + height;  // move downwards
    }
}