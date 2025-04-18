// GameModel.java
package game;

import ecs.Entities.Entity;
import ecs.Entities.Object;
import ecs.Entities.Object.ObjectType;
import ecs.Systems.Collision;
import ecs.Systems.KeyboardInput;
import ecs.Systems.Movement;
import ecs.Systems.Renderer;
import ecs.Systems.Undo;
import edu.usu.graphics.Graphics2D;
import org.joml.Vector3i;

public class GameModel {
    private static final int GRID_WIDTH  = 5;
    private static final int GRID_HEIGHT = 5;

    private Renderer      sysRenderer;
    private KeyboardInput sysKeyboard;
    private Movement      sysMovement;
    private Collision     sysCollision;
    private Undo          sysUndo;

    public void initialize(Graphics2D graphics) {
        sysRenderer  = new Renderer(graphics, GRID_WIDTH, GRID_HEIGHT);
        sysCollision = new Collision();
        sysMovement  = new Movement(sysCollision);
        sysKeyboard  = new KeyboardInput(graphics.getWindow(), sysMovement, null);
        sysUndo      = new Undo(sysRenderer, sysCollision, sysMovement, sysKeyboard);
        sysKeyboard.setUndo(sysUndo);

        Entity rock    = Object.create(ObjectType.ROCK,    new Vector3i(4, 3, 1));
        Entity bigBlue = Object.create(ObjectType.BIGBLUE, new Vector3i(4, 4, 2));

        rock.isPush();
        bigBlue.isYou();

        addEntity(rock);
        addEntity(bigBlue);

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
        sysCollision.add(entity);
        sysUndo.add(entity);
    }
}
