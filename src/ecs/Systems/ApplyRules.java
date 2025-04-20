package ecs.Systems;

import ecs.Components.Attributes;
import ecs.Components.Object;
import ecs.Components.Sprite;
import ecs.Entities.Entity;
import ecs.Entities.Noun;
import edu.usu.audio.Sound;
import edu.usu.audio.SoundManager;
import game.GameModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApplyRules extends System {

    private SoundManager audio;
    private Sound winConditionSFX;
    public Boolean changed;

    GameModel gameModel;
    public ApplyRules(GameModel gameModel){
        super(ecs.Components.Object.class);
        this.gameModel = gameModel;

        audio = new SoundManager();
        winConditionSFX = audio.load("winConditionSFX", "resources/audio/Game.ogg", false);
    }
    @Override
    public void update(double elapsedTime) {

    }

    public void applyRules(HashMap<Entity, Entity> typeRules, HashMap<Entity, Entity> AttributeRules) throws InstantiationException, IllegalAccessException {

        List<Entity> toModify = new ArrayList<>(entities.values());
        for(var entity : toModify){

            var entityAttribute = entity.get(Attributes.class).attribute;

            if(entityAttribute != null)
                entity.remove(ecs.Entities.Object.objectAttributes.get(entity.get(Attributes.class).attribute).getClass());
            entity.get(Attributes.class).attribute = null;

            // Changes entity type
            for(var entry : typeRules.entrySet()){
                var key = entry.getKey();
                var type = Noun.nounTypeToObject.get(key.get(ecs.Components.Noun.class).type);
                if(Noun.objectNounTypes.contains(entry.getValue().get(ecs.Components.Noun.class).type) && entity.contains(type)){
                    entity.remove(type);
                    entity.add(Noun.nounTypeToObject.get(entry.getValue().get(ecs.Components.Noun.class).type).newInstance());
                    entity.get(Object.class).type = Noun.nounToObjectType.get(entry.getValue().get(ecs.Components.Noun.class).type);
                    entity.get(Sprite.class).location = ecs.Entities.Object.objectSprite.get(entity.get(Object.class).type);
                }
            }

            // Changes entity attributes
            for(var entry : AttributeRules.entrySet()){
                var key = entry.getKey();
                var attribute = Noun.nounTypeToObject.get(key.get(ecs.Components.Noun.class).type);
                if(!Noun.objectNounTypes.contains(entry.getValue().get(ecs.Components.Noun.class).type) && entity.contains(attribute)){
                    gameModel.removeEntity(entity.getId(),false);
                    entity.add(Noun.nounToAttribute.get(entry.getValue().get(ecs.Components.Noun.class).type).newInstance());
                    entity.get(Attributes.class).attribute = (Noun.nounToAttributeEnum.get(entry.getValue().get(ecs.Components.Noun.class).type));
                    if(Noun.nounToAttributeEnum.get(entry.getValue().get(ecs.Components.Noun.class).type) == ecs.Entities.AttributesEnum.WIN) {
                        // winConditionSFX.play();
                        java.lang.System.out.println("Loaded SFX? " + (winConditionSFX != null));
                        java.lang.System.out.println(Noun.nounToAttributeEnum.get(entry.getValue().get(ecs.Components.Noun.class).type));
                    }
                }
            }
            gameModel.removeEntity(entity.getId(), true);
            gameModel.addEntity(entity);
        }
    }
}
