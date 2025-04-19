package ecs.Systems;

import ecs.Entities.Entity;
import ecs.Entities.Noun;
import ecs.Systems.collisionsystems.WordCollision;
import java.util.Arrays;
import java.util.HashMap;


public class Sentence extends System {
    WordCollision wordCollision;
    ApplyRules applyRules;

    public Sentence(WordCollision wordCollision, ApplyRules applyRules) {
        super(ecs.Components.Verb.class);
        this.wordCollision = wordCollision;
        this.applyRules = applyRules;
    }

    @Override
    public void update(double elapsedTime) {
        HashMap<Entity, Entity> typeSentence = new HashMap<>();
        HashMap<Entity, Entity> attributeSentence = new HashMap<>();

        for(Entity entity : entities.values()){
            var validSentences = wordCollision.findValidSentence(entity);
            // Should always be just two sentences
            for(int i = 0; i < validSentences.length; i+=2) {
                if (validSentences[i] != null && validSentences[i + 1] != null) {
                    if (!Noun.objectNounTypes.contains(validSentences[i+1].get(ecs.Components.Noun.class).type)) {
                        attributeSentence.put(validSentences[i], validSentences[i + 1]);
                    } else if (Noun.objectNounTypes.contains(validSentences[i+1].get(ecs.Components.Noun.class).type)) {
                        typeSentence.put(validSentences[i], validSentences[i + 1]);
                    }
                }
            }
        }


        try {
            applyRules.applyRules(typeSentence, attributeSentence);
        } catch (InstantiationException | IllegalAccessException e) {
            java.lang.System.err.println("Error applying rules: Failed to instantiate or access attribute class.");
            e.printStackTrace();
        }
    }
}