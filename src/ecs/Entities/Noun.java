package ecs.Entities;

import ecs.Components.Component;
import org.joml.Vector3i;

import java.util.Map;

public class Noun {
    public enum NounType {
        BIGBLUE,
        FLAG,
        IS,
        KILL,
        LAVA,
        PUSH,
        ROCK,
        SINK,
        STOP,
        WALL,
        WATER,
        WIN,
        YOU
    }


    private static final Map<Noun.NounType, String> nounSprite = Map.ofEntries(
            Map.entry(Noun.NounType.BIGBLUE, "resources/sprites/word-baba.png"),
            Map.entry(Noun.NounType.FLAG, "resources/sprites/word-flag.png"),
            Map.entry(Noun.NounType.IS, "resources/sprites/word-is.png"),
            Map.entry(Noun.NounType.KILL, "resources/sprites/word-kill.png"),
            Map.entry(Noun.NounType.LAVA, "resources/sprites/word-lava.png"),
            Map.entry(Noun.NounType.PUSH, "resources/sprites/word-push.png"),
            Map.entry(Noun.NounType.ROCK, "resources/sprites/word-rock.png"),
            Map.entry(Noun.NounType.SINK, "resources/sprites/word-sink.png"),
            Map.entry(Noun.NounType.STOP, "resources/sprites/word-stop.png"),
            Map.entry(Noun.NounType.WALL, "resources/sprites/word-wall.png"),
            Map.entry(Noun.NounType.WATER, "resources/sprites/word-water.png"),
            Map.entry(Noun.NounType.WIN, "resources/sprites/word-win.png"),
            Map.entry(Noun.NounType.YOU, "resources/sprites/word-you.png")
    );

    public static Entity create(Noun.NounType type, Vector3i position){
        var entity = new Entity();
        entity.add(new ecs.Components.Sprite(nounSprite.get(type)));
        entity.add(new ecs.Components.Position(position.x, position.y, position.z));
        entity.add(new ecs.Components.Noun(type));
        entity.add(new ecs.Components.Attributes(AttributesEnum.PUSHABLE));
        entity.add(new ecs.Components.objectattributes.Push());
        entity.add(new ecs.Components.Render());
        entity.add(new ecs.Components.IsUndoable());

        return entity;
    }
}
