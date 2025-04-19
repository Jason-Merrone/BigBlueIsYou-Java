package game;

import edu.usu.graphics.Color;
import edu.usu.graphics.Font;
import edu.usu.graphics.Graphics2D;

import static org.lwjgl.glfw.GLFW.*;

public class AboutView extends GameStateView {

    private KeyboardInput inputKeyboard;
    private GameStateEnum nextGameState = GameStateEnum.About;
    private Font font;

    @Override
    public void initialize(Graphics2D graphics, LevelReader gameLevels, InputConfig inputConfig) {
        super.initialize(graphics, gameLevels, inputConfig);

        font = new Font("resources/fonts/Gaegu-Regular.ttf", 48, false);

        inputKeyboard = new KeyboardInput(graphics.getWindow());
        // When ESC is pressed, set the appropriate new game state
        inputKeyboard.registerCommand(GLFW_KEY_ESCAPE, true, (double elapsedTime) -> {
            nextGameState = GameStateEnum.MainMenu;
        });
    }

    @Override
    public void initializeSession() {
        nextGameState = GameStateEnum.About;
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
/*
    @Override
    public void render(double elapsedTime) {
        final String message = "Outside of assets, audio, the idea for the game";
        final float height = 0.075f;
        final float width = font.measureTextWidth(message, height);

        graphics.drawTextByHeight(font, message, 0.0f - width / 2, 0 - height / 2, height, Color.YELLOW);
    }

 */

    @Override
    public void render(double elapsedTime) {
        final float HEIGHT_MENU_ITEM = 0.075f;
        float top = -0.1f;

        float width = font.measureTextWidth("Credits", 0.12f);
        graphics.drawTextByHeight(font, "Credits", 0.0f - width / 2, -0.3f, 0.12f, Color.LIGHT_BLUE);

        top = renderMenuItem(font, "Menuing, Controls, Loading - Tyler", top, HEIGHT_MENU_ITEM, Color.WHITE);
        top = renderMenuItem(font, "Game logic, Undo, ECS - Jason", top, HEIGHT_MENU_ITEM, Color.WHITE);
        top = renderMenuItem(font, "", top, HEIGHT_MENU_ITEM, Color.WHITE);
        renderMenuItem(font, "Game based on BABA IS YOU", top, HEIGHT_MENU_ITEM, Color.WHITE);
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
