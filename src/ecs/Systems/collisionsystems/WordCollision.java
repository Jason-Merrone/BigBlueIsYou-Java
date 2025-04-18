package ecs.Systems.collisionsystems;

import ecs.Entities.Entity;
import ecs.Systems.System;

public class WordCollision extends System {
    public WordCollision() {
        super(ecs.Components.Noun.class);
    }

    @Override
    public void update(double elapsedTime) {
    }

    public Entity[] findValidSentences(Entity entity) {
        var pos = entity.get(ecs.Components.Position.class);
        for(Entity other : entities.values()){
            var pos2 = other.get(ecs.Components.Position.class);
            if(pos.x == pos2.x && pos.y == pos2.y && entity != other){

            }
        }
        return null;
    }
}
