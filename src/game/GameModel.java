// GameModel.java
package game;

import ecs.Components.objecttypes.BigBlue;
import ecs.Entities.*;
import ecs.Entities.Object;
import ecs.Entities.Object.ObjectType;
import ecs.Systems.collisionsystems.PushableCollision;
import ecs.Systems.KeyboardInput;
import ecs.Systems.Movement;
import ecs.Systems.Renderer;
import ecs.Systems.Undo;
import ecs.Systems.collisionsystems.StoppedCollision;
import edu.usu.graphics.Graphics2D;
import org.joml.Vector3i;

public class GameModel {
    private static final int GRID_WIDTH  = 10;
    private static final int GRID_HEIGHT = 10;

    private Renderer      sysRenderer;
    private KeyboardInput sysKeyboard;
    private Movement      sysMovement;
    private PushableCollision sysPushableCollision;
    private StoppedCollision sysStoppedCollision;
    private Undo          sysUndo;

    public void initialize(Graphics2D graphics) {
        sysRenderer  = new Renderer(graphics, GRID_WIDTH, GRID_HEIGHT);
        sysPushableCollision = new PushableCollision();
        sysStoppedCollision = new StoppedCollision();
        sysMovement  = new Movement(sysPushableCollision, sysStoppedCollision);
        sysKeyboard  = new KeyboardInput(graphics.getWindow(), sysMovement, null);
        sysUndo      = new Undo(sysRenderer, sysPushableCollision, sysMovement, sysKeyboard, sysStoppedCollision);
        sysKeyboard.setUndo(sysUndo);

        Entity rock    = Object.create(ObjectType.ROCK,    new Vector3i(4, 3, 1));
        Entity bigBlue = Object.create(ObjectType.BIGBLUE, new Vector3i(4, 4, 2));

        Entity is = Verb.create(Verb.VerbType.IS,new Vector3i(1, 1, 1));
        Entity you = Noun.create(Noun.NounType.YOU,new Vector3i(2, 1, 1));
        Entity baba = Noun.create(Noun.NounType.BIGBLUE,new Vector3i(0, 1, 1));

        rock.get(ecs.Components.Attributes.class).addAttribute(AttributesEnum.STOP);
        rock.add(new ecs.Components.objectattributes.Stop());
        bigBlue.get(ecs.Components.Attributes.class).addAttribute(AttributesEnum.YOU);
        bigBlue.add(new ecs.Components.objectattributes.You());

        addEntity(rock);
        addEntity(bigBlue);
        addEntity(is);
        addEntity(you);
        addEntity(baba);

        sysUndo.push();
    }

    public void update(double elapsedTime) {
        sysKeyboard.update(elapsedTime);
        sysRenderer.update(elapsedTime);
    }

    private void addEntity(Entity entity) {
        sysRenderer.add(entity);
        sysKeyboard.add(entity);
        sysMovement.add(entity);
        sysPushableCollision.add(entity);
        sysStoppedCollision.add(entity);
        sysUndo.add(entity);
    }
}
