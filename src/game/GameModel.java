// GameModel.java
package game;

import ecs.Components.objecttypes.BigBlue;
import ecs.Entities.*;
import ecs.Entities.Object;
import ecs.Entities.Object.ObjectType;
import ecs.Systems.*;
import ecs.Systems.KeyboardInput;
import ecs.Systems.collisionsystems.PushableCollision;
import ecs.Systems.collisionsystems.StoppedCollision;
import ecs.Systems.collisionsystems.WordCollision;
import edu.usu.graphics.Graphics2D;
import org.joml.Vector3i;

import java.util.ArrayList;

public class GameModel {
    private static final int GRID_WIDTH  = 10;
    private static final int GRID_HEIGHT = 10;

    private Renderer      sysRenderer;
    private KeyboardInput sysKeyboard;
    private Movement      sysMovement;
    private PushableCollision sysPushableCollision;
    private StoppedCollision sysStoppedCollision;
    private WordCollision sysWordCollision;
    private Sentence sysSentence;
    private ApplyRules sysApplyRules;
    private Undo          sysUndo;
    private ArrayList<Entity> entities = new ArrayList<>();

    public void initialize(Graphics2D graphics) {
        sysRenderer  = new Renderer(graphics, GRID_WIDTH, GRID_HEIGHT);
        sysPushableCollision = new PushableCollision();
        sysStoppedCollision = new StoppedCollision();
        sysWordCollision = new WordCollision();
        sysApplyRules = new ApplyRules(this);
        sysSentence = new Sentence(sysWordCollision, sysApplyRules);
        sysMovement  = new Movement(sysPushableCollision, sysStoppedCollision, null);
        sysKeyboard  = new KeyboardInput(graphics.getWindow(), sysMovement, null, sysSentence);
        sysUndo      = new Undo(sysRenderer, sysPushableCollision, sysMovement, sysKeyboard, sysStoppedCollision,sysWordCollision,sysSentence, sysApplyRules);
        sysMovement.setUndo(sysUndo);
        sysKeyboard.setUndo(sysUndo);

        Entity rock    = Object.create(ObjectType.ROCK,    new Vector3i(4, 3, 1));
        Entity bigBlue = Object.create(ObjectType.BIGBLUE, new Vector3i(4, 4, 2));

        Entity is = Verb.create(Verb.VerbType.IS,new Vector3i(1, 1, 1));
        Entity you = Noun.create(Noun.NounType.YOU,new Vector3i(2, 1, 1));
        Entity baba = Noun.create(Noun.NounType.BIGBLUE,new Vector3i(0, 1, 1));

        Entity rockWord = Noun.create(Noun.NounType.ROCK,new Vector3i(4, 1, 1));
        Entity is2 = Verb.create(Verb.VerbType.IS,new Vector3i(5, 1, 1));
        Entity stopWord = Noun.create(Noun.NounType.STOP,new Vector3i(6, 1, 1));


        addEntity(rock);
        addEntity(bigBlue);
        addEntity(is);
        addEntity(you);
        addEntity(baba);
        addEntity(rockWord);
        addEntity(is2);
        addEntity(stopWord);

        sysUndo.push();
    }

    public void update(double elapsedTime) {
        sysKeyboard.update(elapsedTime);
        sysRenderer.update(elapsedTime);
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
        sysUndo.remove(id);
        if(!ignoreApplyRules)
            sysApplyRules.remove(id);
    }
}
