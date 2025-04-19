package game;

import ecs.Entities.Entity;
import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class GamePlayView extends GameStateView {

    private KeyboardInput inputKeyboard;
    private GameStateEnum nextGameState = GameStateEnum.GamePlay;
    private GameModel gameModel;

    @Override
    public void initialize(Graphics2D graphics, LevelReader gameLevels, InputConfig inputConfig) {
        super.initialize(graphics, gameLevels, inputConfig);

        inputKeyboard = new KeyboardInput(graphics.getWindow());
        // When ESC is pressed, set the appropriate new game state
        inputKeyboard.registerCommand(GLFW_KEY_ESCAPE, true, (double elapsedTime) -> {
            nextGameState = GameStateEnum.MainMenu;
        });
    }

    @Override
    public void initializeSession() {
        gameModel = new GameModel();
        gameModel.initialize(graphics, gameLevels);
        nextGameState = GameStateEnum.GamePlay;
    }

    @Override
    public GameStateEnum processInput(double elapsedTime) {
        // Updating the keyboard can change the nextGameState
        inputKeyboard.update(elapsedTime);
        return nextGameState;
    }

    @Override
    public void update(double elapsedTime) {
        gameModel.update(elapsedTime);
    }

    @Override
    public void render(double elapsedTime) {
        // Nothing to do because the render now occurs in the update of the game model
    }
/*
    private void renderWin() {
        float HEIGHT = 1.0f;
        float width = font.measureTextWidth("You Win", HEIGHT);
        graphics.drawTextByHeight(font, "You Win", 0.0f - width / 2, -0.1, HEIGHT, Color.LIGHT_YELLOW);
    }
    private void renderLose() {
        float HEIGHT = 1.0f;
        float width = font.measureTextWidth("You Lose", HEIGHT);
        graphics.drawTextByHeight(font, "You Lose", 0.0f - width / 2, -0.1, HEIGHT, Color.LIGHT_BLUE);
    }
    
 */
}
