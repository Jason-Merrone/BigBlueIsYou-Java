package ecs.Entities;

import ecs.Components.Component;
import org.joml.Vector3i;

import java.util.Arrays;
import java.util.HashSet;
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

    public static final HashSet<NounType> objectNounTypes = new HashSet<>(Arrays.asList(NounType.ROCK, NounType.BIGBLUE, NounType.FLAG,NounType.LAVA,NounType.WALL, NounType.WATER));

    public static final Map<NounType,Class<? extends Component>> nounTypeToObject = Map.ofEntries(
            Map.entry(Noun.NounType.BIGBLUE,ecs.Components.objecttypes.BigBlue.class),
            Map.entry(Noun.NounType.FLAG,ecs.Components.objecttypes.Flag.class),
            Map.entry(Noun.NounType.LAVA,ecs.Components.objecttypes.Lava.class),
            Map.entry(Noun.NounType.ROCK,ecs.Components.objecttypes.Rock.class),
            Map.entry(Noun.NounType.WALL,ecs.Components.objecttypes.Wall.class),
            Map.entry(Noun.NounType.WATER,ecs.Components.objecttypes.Water.class)
    );

    public static Map<NounType, Class<? extends Component>> nounToAttribute = Map.ofEntries(
            Map.entry(NounType.KILL, ecs.Components.objectattributes.Kill.class),
            Map.entry(NounType.PUSH, ecs.Components.objectattributes.Push.class),
            Map.entry(NounType.SINK, ecs.Components.objectattributes.Sink.class),
            Map.entry(NounType.STOP, ecs.Components.objectattributes.Stop.class),
            Map.entry(NounType.WIN, ecs.Components.objectattributes.Win.class),
            Map.entry(NounType.YOU, ecs.Components.objectattributes.You.class)
    );

    public static Map<NounType, AttributesEnum> nounToAttributeEnum = Map.ofEntries(
            Map.entry(NounType.KILL, AttributesEnum.KILL),
            Map.entry(NounType.PUSH, AttributesEnum.PUSHABLE),
            Map.entry(NounType.SINK, AttributesEnum.SINK),
            Map.entry(NounType.STOP, AttributesEnum.STOP),
            Map.entry(NounType.WIN, AttributesEnum.WIN),
            Map.entry(NounType.YOU, AttributesEnum.YOU)
    );

    public static Map<NounType, Object.ObjectType> nounToObjectType= Map.ofEntries(
            Map.entry(Noun.NounType.BIGBLUE,Object.ObjectType.BIGBLUE),
            Map.entry(Noun.NounType.FLAG,Object.ObjectType.FLAG),
            Map.entry(Noun.NounType.LAVA,Object.ObjectType.LAVA),
            Map.entry(Noun.NounType.ROCK,Object.ObjectType.ROCK),
            Map.entry(Noun.NounType.WALL,Object.ObjectType.WALL),
            Map.entry(Noun.NounType.WATER,Object.ObjectType.WATER)
    );


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
