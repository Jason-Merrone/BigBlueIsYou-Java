package game;

import edu.usu.graphics.Graphics2D;

public abstract class GameStateView implements IGameState {
    protected Graphics2D graphics;
    protected LevelReader gameLevels;
    protected InputConfig inputConfig;

    @Override
    public void initialize(Graphics2D graphics, LevelReader gameLevels, InputConfig inputConfig) {
        this.graphics = graphics;
        this.gameLevels = gameLevels;
        this.inputConfig = inputConfig;
    }

    @Override
    public void initializeSession() {};

    @Override
    public abstract GameStateEnum processInput(double elapsedTime);

    @Override
    public abstract void update(double elapsedTime);

    @Override
    public abstract void render(double elapsedTime);
}
