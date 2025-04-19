package game;

import edu.usu.graphics.Graphics2D;

public interface IGameState {
    void initialize(Graphics2D graphics, LevelReader gameLevels, InputConfig inputConfig);

    void initializeSession();

    GameStateEnum processInput(double elapsedTime);

    void update(double elapsedTime);

    void render(double elapsedTime);
}
