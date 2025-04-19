package game;

import edu.usu.graphics.Color;
import edu.usu.graphics.Font;
import edu.usu.graphics.Graphics2D;

import static org.lwjgl.glfw.GLFW.*;

import java.util.List;

public class LevelSelectView extends GameStateView {

    private List<LevelReader.LevelDescriptor> levels;
    private int currentSelection = 0;

    private KeyboardInput inputKeyboard;
    private GameStateEnum nextGameState = GameStateEnum.LevelSelect;

    private Font fontMenu;
    private Font fontSelected;

    @Override
    public void initialize(Graphics2D graphics, LevelReader gameLevels, InputConfig inputConfig) {
        super.initialize(graphics, gameLevels, inputConfig);

        // load fonts
        fontMenu     = new Font("resources/fonts/Gaegu-Regular.ttf", 48, false);
        fontSelected = new Font("resources/fonts/Gaegu-Bold.ttf",    48, false);

        // grab your level descriptors
        levels = gameLevels.getLevelDescriptors();
    }

    @Override
    public void initializeSession() {
        // whenever this view is (re)entered, reset state if you like:
        nextGameState    = GameStateEnum.LevelSelect;
        currentSelection = 0;
        initializeKeys();
    }

    private void initializeKeys() {
        inputKeyboard = new KeyboardInput(graphics.getWindow());

        // navigate up
        inputKeyboard.registerCommand(
                inputConfig.getActionKey(InputConfig.Action.UP),
                true,
                (dt) -> currentSelection = (currentSelection - 1 + levels.size()) % levels.size()
        );

        // navigate down
        inputKeyboard.registerCommand(
                inputConfig.getActionKey(InputConfig.Action.DOWN),
                true,
                (dt) -> currentSelection = (currentSelection + 1) % levels.size()
        );

        // select (start) level
        inputKeyboard.registerCommand(
                GLFW_KEY_ENTER,
                true,
                (dt) -> {
                    // you might need to tell your LevelReader which level was chosen:
                    gameLevels.setLevelDescriptor(levels.get(currentSelection));
                    nextGameState = GameStateEnum.GamePlay;
                }
        );

        // back to main menu
        inputKeyboard.registerCommand(
                GLFW_KEY_ESCAPE,
                true,
                (dt) -> nextGameState = GameStateEnum.MainMenu
        );
    }

    @Override
    public GameStateEnum processInput(double elapsedTime) {
        inputKeyboard.update(elapsedTime);
        return nextGameState;
    }

    @Override
    public void update(double elapsedTime) {
        // no per-frame logic needed here
    }

    @Override
    public void render(double elapsedTime) {
        final float ITEM_HEIGHT = 0.075f;
        float top = -0.25f;

        for (int i = 0; i < levels.size(); i++) {
            boolean isSelected = (i == currentSelection);
            Font font = isSelected ? fontSelected : fontMenu;
            Color color = isSelected ? Color.WHITE : Color.LIGHT_GRAY;
            top = renderMenuItem(font, levels.get(i).getName(), top, ITEM_HEIGHT, color);
        }
    }

    /**
     * Centers the text horizontally at the given vertical position, then returns
     * the next vertical position.
     */
    private float renderMenuItem(Font font, String text, float top, float height, Color color) {
        float trueHeight = height * 1.3f;
        float width = font.measureTextWidth(text, trueHeight);
        graphics.drawTextByHeight(font, text, -width / 2, top, trueHeight, color);
        return top + height;
    }
}