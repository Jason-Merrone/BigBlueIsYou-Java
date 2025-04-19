package ecs.Systems.collisionsystems;

import ecs.Entities.Entity;
import ecs.Systems.System;

public class SinkCollision extends System {

    public SinkCollision() {
        super(ecs.Components.objectattributes.Sink.class);
    }

    @Override
    public void update(double elapsedTime) {
    }

    public Entity collidesWith(Entity entity) {
        var pos = entity.get(ecs.Components.Position.class);
        for(Entity other : entities.values()){
            var pos2 = other.get(ecs.Components.Position.class);
            if(pos.x == pos2.x && pos.y == pos2.y && entity != other){
                return other;
            }
        }
        return null;
    }
}
