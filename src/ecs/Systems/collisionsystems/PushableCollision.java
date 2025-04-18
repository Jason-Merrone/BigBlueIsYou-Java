package ecs.Systems.collisionsystems;

import ecs.Entities.Entity;
import ecs.Systems.System;

public class PushableCollision extends System {

    public PushableCollision() {
        super(ecs.Components.objectattributes.Push.class);
    }

    @Override
    public void update(double elapsedTime) {
    }

    public Entity collidesWith(ecs.Entities.Entity entity) {
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
