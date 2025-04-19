package ecs.Systems;

import ecs.Components.Noun;
import ecs.Components.objectattributes.Kill;
import ecs.Components.objectattributes.Sink;
import ecs.Entities.Entity;
import ecs.Systems.collisionsystems.*;

import java.util.ArrayList;

public class Movement extends System{

    public enum Direction { UP, DOWN, LEFT, RIGHT }

    Direction direction = Direction.UP;

    PushableCollision pushableCollision;
    StoppedCollision stoppedCollision;
    WinCollision winCollision;
    KillCollision killCollision;
    SinkCollision sinkCollision;
    Undo undo;

    public boolean isWin;
    public ArrayList<Entity> killed = new ArrayList<>();
    public ArrayList<Entity> sunk = new ArrayList<>();

    public Movement(PushableCollision collision, StoppedCollision stoppedCollision, Undo undo, WinCollision winCollision, KillCollision killCollision, SinkCollision sinkCollision) {
        super(ecs.Components.objectattributes.You.class);
        this.pushableCollision = collision;
        this.stoppedCollision = stoppedCollision;
        this.undo = undo;
        this.winCollision = winCollision;
        this.killCollision = killCollision;
        this.sinkCollision = sinkCollision;
    }


    public void setUndo(Undo undo) {
        this.undo = undo;
    }

    @Override
    public void update(double elapsedTime) {

        boolean updatedUndo= false;
        for (var entity : entities.values()) {
            if(canMove(entity)) {
                if(!updatedUndo) {
                    undo.push();
                    updatedUndo = true;
                }
                updateLocation(entity, false);
            }
            if(killCollision.collidesWith(entity) != null){
                killed.add(entity);
            }
            if(winCollision.collidesWith(entity) != null){
                isWin = true;
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
           var sinkEntity = sinkCollision.collidesWith(entity);

           if(sinkEntity != null && !entity.contains(ecs.Components.Noun.class) && !entity.contains(ecs.Components.Verb.class)){
               sunk.add(entity);
               sunk.add(sinkEntity);
           }

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
        var selfCollidedWith = collidesWith(newEntity);
        if(pushableCollidedWith != null)
            return canMove(pushableCollidedWith);
        if(selfCollidedWith != null)
            return canMove(selfCollidedWith);

        return stoppedCollidedWith == null;
    }

    public void updateDirection(Direction direction){
        this.direction = direction;
    }

    private Entity collidesWith(Entity entity){
        var pos = entity.get(ecs.Components.Position.class);
        for(ecs.Entities.Entity other : entities.values()){
            var pos2 = other.get(ecs.Components.Position.class);
            if(pos.x == pos2.x && pos.y == pos2.y && entity != other){
                return other;
            }
        }
        return null;
    }
}
