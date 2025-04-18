package ecs.Systems;

import ecs.Components.nouns.IsYou;
import ecs.Entities.Entity;

public class Movement extends System{

    public enum Direction { UP, DOWN, LEFT, RIGHT }

    Direction direction = Direction.UP;

    Collision collision;

    public Movement(Collision collision) {
        super(ecs.Components.Movable.class);
        this.collision = collision;
    }
    @Override
    public void update(double elapsedTime) {
        for (var entity : entities.values()) {
           updateLocation(entity, 1, false);
        }


    }

    private void updateLocation(Entity entity, int factor, boolean ignoreClass) {
       var pos = entity.get(ecs.Components.Position.class);

       if (direction == Direction.UP && ( entity.contains(IsYou.class) || ignoreClass) ){
           pos.y -= factor;
       } else if (direction == Direction.DOWN && ( entity.contains(IsYou.class) || ignoreClass)) {
           pos.y += factor;
       } else if (direction == Direction.LEFT && ( entity.contains(IsYou.class) || ignoreClass)) {
           pos.x -= factor;
       } else if (direction == Direction.RIGHT && ( entity.contains(IsYou.class) || ignoreClass)) {
           pos.x += factor;
       }

       var collidedWith = collision.collidesWith(entity);

       if(collidedWith != null){
           if(collidedWith.contains(ecs.Components.nouns.IsPush.class))
               updateLocation(collidedWith, 1, true);
           else if(collidedWith.contains(ecs.Components.nouns.IsStop.class))
               updateLocation(entity, -1, false);
       }
    }

    public void updateDirection(Direction direction){
        this.direction = direction;
    }
}
