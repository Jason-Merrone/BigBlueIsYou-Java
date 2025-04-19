package ecs.Systems.collisionsystems;

import ecs.Components.Noun;
import ecs.Entities.Entity;
import ecs.Systems.System;

import java.util.Arrays;

public class WordCollision extends System {
    public WordCollision() {
        super(ecs.Components.Noun.class);
    }

    @Override
    public void update(double elapsedTime) {
    }

    public Entity[] findValidSentence(Entity entity) {
        Entity[] validSentences = new Entity[4];
        var pos = entity.get(ecs.Components.Position.class);
        for(Entity other : entities.values()){
            var pos2 = other.get(ecs.Components.Position.class);
            if(pos.x-1 == pos2.x && pos.y == pos2.y){
                validSentences[0] = other;
            } else if (pos.x+1 == pos2.x && pos.y == pos2.y) {
                validSentences[1] = other;
            } else if (pos.x == pos2.x && pos.y+1 == pos2.y) {
                validSentences[2] = other;
            } else if (pos.x == pos2.x && pos.y-1 == pos2.y) {
                validSentences[3] = other;
            }
        }

        if(validSentences[0] != null && !ecs.Entities.Noun.objectNounTypes.contains(validSentences[0].get(Noun.class).type)){
            validSentences[0] = null;
        }
        else if(validSentences[2] != null && !ecs.Entities.Noun.objectNounTypes.contains(validSentences[2].get(Noun.class).type)){
            validSentences[2] = null;
        }

        return validSentences;
    }
}
