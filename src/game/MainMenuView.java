package game;

import edu.usu.graphics.Color;
import edu.usu.graphics.Font;
import edu.usu.graphics.Graphics2D;

import static org.lwjgl.glfw.GLFW.*;

public class MainMenuView extends GameStateView {

    private enum MenuState {
        NewGame,
        Controls,
        About,
        Quit;

        public MenuState next() {
            int nextOrdinal = (this.ordinal() + 1) % MenuState.values().length;
            return MenuState.values()[nextOrdinal];
        }

        public MenuState previous() {
            int previousOrdinal = (this.ordinal() - 1) % MenuState.values().length;
            if (previousOrdinal < 0) {
                previousOrdinal = Quit.ordinal();
            }
            return MenuState.values()[previousOrdinal];
        }
    }

    private MenuState currentSelection = MenuState.NewGame;
    private KeyboardInput inputKeyboard;
    private GameStateEnum nextGameState = GameStateEnum.MainMenu;
    private Font fontMenu;
    private Font fontSelected;

    @Override
    public void initialize(Graphics2D graphics, LevelReader gameLevels, InputConfig inputConfig) {
        super.initialize(graphics, gameLevels, inputConfig);

        fontMenu = new Font("resources/fonts/Gaegu-Regular.ttf", 48, false);
        fontSelected = new Font("resources/fonts/Gaegu-Bold.ttf", 48, false);
    }

    @Override
    public void initializeSession() {
        initializeKeys();
        nextGameState = GameStateEnum.MainMenu;
    }

    void initializeKeys() {
        inputKeyboard = new KeyboardInput(graphics.getWindow());
        // Arrow keys to navigate the menu
        inputKeyboard.registerCommand(inputConfig.getActionKey(InputConfig.Action.UP), true, (double elapsedTime) -> {
            currentSelection = currentSelection.previous();
        });
        inputKeyboard.registerCommand(inputConfig.getActionKey(InputConfig.Action.DOWN), true, (double elapsedTime) -> {
            currentSelection = currentSelection.next();
        });
        // When Enter is pressed, set the appropriate new game state
        inputKeyboard.registerCommand(GLFW_KEY_ENTER, true, (double elapsedTime) -> {
            nextGameState = switch (currentSelection) {
                case MenuState.NewGame -> GameStateEnum.LevelSelect;
                case MenuState.Controls -> GameStateEnum.Controls;
                case MenuState.About -> GameStateEnum.About;
                case MenuState.Quit -> GameStateEnum.Quit;
            };
        });
    }

    @Override
    public GameStateEnum processInput(double elapsedTime) {
        // Updating the keyboard can change the nextGameState
        inputKeyboard.update(elapsedTime);
        return nextGameState;
    }

    @Override
    public void update(double elapsedTime) {
    }

    @Override
    public void render(double elapsedTime) {
        final float HEIGHT_MENU_ITEM = 0.075f;
        float top = -0.1f;

        float width = fontMenu.measureTextWidth("BigBlue IS YOU", 0.12f);
        graphics.drawTextByHeight(fontSelected, "BigBlue IS YOU", 0.0f - width / 2, -0.3f, 0.12f, Color.LIGHT_BLUE);

        top = renderMenuItem(currentSelection == MenuState.NewGame ? fontSelected : fontMenu, "New Game", top, HEIGHT_MENU_ITEM, currentSelection == MenuState.NewGame ? Color.WHITE : Color.LIGHT_GRAY);
        top = renderMenuItem(currentSelection == MenuState.Controls ? fontSelected : fontMenu, "Controls", top, HEIGHT_MENU_ITEM, currentSelection == MenuState.Controls ? Color.WHITE : Color.LIGHT_GRAY);
        top = renderMenuItem(currentSelection == MenuState.About ? fontSelected : fontMenu, "Credits", top, HEIGHT_MENU_ITEM, currentSelection == MenuState.About ? Color.WHITE : Color.LIGHT_GRAY);
        renderMenuItem(currentSelection == MenuState.Quit ? fontSelected : fontMenu, "Quit", top, HEIGHT_MENU_ITEM, currentSelection == MenuState.Quit ? Color.WHITE : Color.LIGHT_GRAY);
    }

    /**
     * Centers the text horizontally, at the specified top position.
     * It also returns the vertical position to draw the next menu item
     */
    private float renderMenuItem(Font font, String text, float top, float height, Color color) {
        float trueHeight = height * 1.3f;
        float width = font.measureTextWidth(text, trueHeight);
        graphics.drawTextByHeight(font, text, 0.0f - width / 2, top, trueHeight, color);

        return top + height;
    }
}
