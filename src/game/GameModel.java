// GameModel.java
package game;

import ecs.Components.objecttypes.BigBlue;
import ecs.Entities.*;
import ecs.Entities.Object;
import ecs.Entities.Object.ObjectType;
import ecs.Systems.*;
import ecs.Systems.KeyboardInput;
import ecs.Systems.collisionsystems.*;
import edu.usu.graphics.Graphics2D;
import org.joml.Vector3i;

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


    public void initialize(Graphics2D graphics, LevelReader gameLevels) {
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
        sysKeyboard  = new KeyboardInput(graphics.getWindow(), sysMovement, null, sysSentence);
        sysUndo      = new Undo(sysRenderer, sysPushableCollision, sysMovement, sysKeyboard, sysStoppedCollision,sysWordCollision,sysSentence, sysApplyRules, sysWinCollision, sysKillCollision, sysSinkCollision);
        sysMovement.setUndo(sysUndo);
        sysKeyboard.setUndo(sysUndo);

        List<Entity> entities = gameLevels.loadLevelEntities(gameLevels.getCurrentDescriptor());

        for (Entity entity : entities) {
            addEntity(entity);
        }

        sysSentence.update(0);
        sysUndo.push();
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
}
