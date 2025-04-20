// GameModel.java
package game;

import ecs.Components.objecttypes.BigBlue;
import ecs.Entities.*;
import ecs.Entities.Object;
import ecs.Entities.Object.ObjectType;
import ecs.Systems.*;
import ecs.Systems.KeyboardInput;
import ecs.Systems.collisionsystems.*;
import edu.usu.audio.Sound;
import edu.usu.audio.SoundManager;
import edu.usu.graphics.Font;
import edu.usu.graphics.Graphics2D;
import org.joml.Vector3i;
import edu.usu.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private static final int GRID_WIDTH  = 10;
    private static final int GRID_HEIGHT = 10;

    private Renderer      sysRenderer;
    private KeyboardInput sysKeyboard;
    private Movement      sysMovement;
    private PushableCollision sysPushableCollision;
    private StoppedCollision sysStoppedCollision;
    private WordCollision sysWordCollision;
    private KillCollision sysKillCollision;
    private WinCollision sysWinCollision;
    private SinkCollision sysSinkCollision;
    private Sentence sysSentence;
    private ApplyRules sysApplyRules;
    private Undo          sysUndo;

    private Graphics2D graphics;
    private InputConfig inputConfig;

    private Font fontBold;

    private SoundManager audio;
    private Sound winConditionSFX;


    public void initialize(Graphics2D graphics, LevelReader gameLevels, SoundManager soundManager, InputConfig inputConfig) {
        this.graphics = graphics;
        this.audio = soundManager;
        this.inputConfig = inputConfig;

        sysRenderer  = new Renderer(graphics, gameLevels.getCurrentDescriptor().getWidth(), gameLevels.getCurrentDescriptor().getHeight());
        sysPushableCollision = new PushableCollision();
        sysStoppedCollision = new StoppedCollision();
        sysWordCollision = new WordCollision();
        sysKillCollision = new KillCollision();
        sysWinCollision = new WinCollision();
        sysSinkCollision = new SinkCollision();
        sysApplyRules = new ApplyRules(this);
        sysSentence = new Sentence(sysWordCollision, sysApplyRules);
        sysMovement  = new Movement(sysPushableCollision, sysStoppedCollision, null, sysWinCollision, sysKillCollision, sysSinkCollision);
        sysKeyboard  = new KeyboardInput(graphics.getWindow(), sysMovement, null, sysSentence, inputConfig);
        sysUndo      = new Undo(sysRenderer, sysPushableCollision, sysMovement, sysKeyboard, sysStoppedCollision,sysWordCollision,sysSentence, sysApplyRules, sysWinCollision, sysKillCollision, sysSinkCollision);
        sysMovement.setUndo(sysUndo);
        sysKeyboard.setUndo(sysUndo);

        List<Entity> entities = gameLevels.loadLevelEntities(gameLevels.getCurrentDescriptor());

        for (Entity entity : entities) {
            addEntity(entity);
        }

        sysSentence.update(0);
        sysUndo.push();

        fontBold = new Font("resources/fonts/Gaegu-Bold.ttf",    48, true);

        winConditionSFX = audio.load("winConditionSFX", "resources/audio/WinCondition.ogg", false);
        //winConditionSFX.play();
    }

    public void update(double elapsedTime) {

        if(!sysMovement.isWin)
            sysKeyboard.update(elapsedTime);
        sysRenderer.update(elapsedTime);
        for(var entity : sysMovement.killed){
            removeEntity(entity.getId(), false);
        }
        sysMovement.killed.clear();

        for(var entity : sysMovement.sunk){
            removeEntity(entity.getId(), false);
        }
        sysMovement.sunk.clear();
        if (sysApplyRules.winConditionUpdated) {
            winConditionSFX.play();
            sysApplyRules.winConditionUpdated = false;
        }
    }

    public void addEntity(Entity entity) {
        sysRenderer.add(entity);
        sysKeyboard.add(entity);
        sysMovement.add(entity);
        sysPushableCollision.add(entity);
        sysStoppedCollision.add(entity);
        sysSentence.add(entity);
        sysWordCollision.add(entity);
        sysApplyRules.add(entity);
        sysKillCollision.add(entity);
        sysWinCollision.add(entity);
        sysSinkCollision.add(entity);
        sysUndo.add(entity);
    }

    public void removeEntity(long id, boolean ignoreApplyRules) {
        sysRenderer.remove(id);
        sysKeyboard.remove(id);
        sysMovement.remove(id);
        sysPushableCollision.remove(id);
        sysStoppedCollision.remove(id);
        sysSentence.remove(id);
        sysWordCollision.remove(id);
        sysKillCollision.remove(id);
        sysWinCollision.remove(id);
        sysSinkCollision.remove(id);
        sysUndo.remove(id);
        if(!ignoreApplyRules)
            sysApplyRules.remove(id);
    }

    public void render(double elapsedTime) {
        if (sysMovement.isWin) {
            renderWin();
        }
    }

    private void renderWin() {
        float HEIGHT = 0.2f;
        float width = fontBold.measureTextWidth("You Win", HEIGHT);
        graphics.drawTextByHeight(fontBold, "You Win", 0.0f - width / 2, -0.1f, HEIGHT, Color.LIGHT_YELLOW);
    }
}
