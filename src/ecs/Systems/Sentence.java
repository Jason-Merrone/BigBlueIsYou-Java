package ecs.Systems;

import ecs.Components.Component;
import ecs.Components.Verb;
import ecs.Entities.Entity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Sentence extends System {

    public Sentence() {
        super(ecs.Components.Verb.class);

    }

    @Override
    public void update(double elapsedTime) {
        HashMap<Entity, Entity> map = new HashMap<>();

        for(var word : entities.values()){
            if(word.get(Verb.class).type == ecs.Entities.Verb.VerbType.IS) {
                var verticalSentence = sentenceComponents(word,true);
                var horizontalSentence = sentenceComponents(word,false);

                if(verticalSentence != null)
                    map.putIfAbsent(verticalSentence[0], verticalSentence[1]);

                if(horizontalSentence != null)
                    map.putIfAbsent(horizontalSentence[0], horizontalSentence[1]);
            }
        }


    }

    private Entity[] sentenceComponents(Entity isComponent, Boolean isVertical){
        return null;
    }

}