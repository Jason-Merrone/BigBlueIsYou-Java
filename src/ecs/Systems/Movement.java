package ecs.Systems;

import ecs.Components.Noun;
import ecs.Entities.Entity;
import ecs.Systems.collisionsystems.PushableCollision;
import ecs.Systems.collisionsystems.StoppedCollision;

public class Movement extends System{

    public enum Direction { UP, DOWN, LEFT, RIGHT }

    Direction direction = Direction.UP;

    PushableCollision pushableCollision;
    StoppedCollision stoppedCollision;
    Undo undo;

    public Movement(PushableCollision collision, StoppedCollision stoppedCollision, Undo undo) {
        super(ecs.Components.objectattributes.You.class);
        this.pushableCollision = collision;
        this.stoppedCollision = stoppedCollision;
        this.undo = undo;
    }

    public void setUndo(Undo undo) {
        this.undo = undo;
    }

    @Override
    public void update(double elapsedTime) {
        for (var entity : entities.values()) {
            if(canMove(entity)) {
                undo.push();
                updateLocation(entity, false);

            }
        }
    }

    private void updateLocation(Entity entity, Boolean simulation) {
       var pos = entity.get(ecs.Components.Position.class);

       if (direction == Direction.UP ){
           pos.y -= 1;
       } else if (direction == Direction.DOWN) {
           pos.y += 1;
       } else if (direction == Direction.LEFT) {
           pos.x -= 1;
       } else if (direction == Direction.RIGHT) {
           pos.x += 1;
       }

       if(!simulation) {
           var collidedWith = pushableCollision.collidesWith(entity);

           if (collidedWith != null)
               updateLocation(collidedWith, false);
       }
    }

    private boolean canMove(Entity entity) {
        Entity newEntity = new Entity(entity);
        updateLocation(newEntity, true);


        var pushableCollidedWith = pushableCollision.collidesWith(newEntity);
        var stoppedCollidedWith = stoppedCollision.collidesWith(newEntity);
        if(pushableCollidedWith != null)
            return canMove(pushableCollidedWith);

        return stoppedCollidedWith == null;
    }

    public void updateDirection(Direction direction){
        this.direction = direction;
    }
}
