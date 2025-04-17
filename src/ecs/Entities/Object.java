package ecs.Entities;

import ecs.Components.Component;
import org.joml.Vector3i;

import java.util.Map;

public class Object {
    public enum ObjectType {
        BIGBLUE,
        FLAG,
        FLOOR,
        FLOWERS,
        GRASS,
        HEDGE,
        LAVA,
        ROCK,
        WALL,
        WATER
    }

    private static final Map<ObjectType, String> objectSprite = Map.ofEntries(
            Map.entry(ObjectType.BIGBLUE, "resources/sprites/bigblue.png"),
            Map.entry(ObjectType.FLAG, "resources/sprites/flag.png"),
            Map.entry(ObjectType.FLOOR, "resources/sprites/floor.png"),
            Map.entry(ObjectType.FLOWERS, "resources/sprites/flowers.png"),
            Map.entry(ObjectType.GRASS, "resources/sprites/grass.png"),
            Map.entry(ObjectType.HEDGE, "resources/sprites/hedge.png"),
            Map.entry(ObjectType.LAVA, "resources/sprites/lava.png"),
            Map.entry(ObjectType.ROCK, "resources/sprites/rock.png"),
            Map.entry(ObjectType.WALL, "resources/sprites/wall.png"),
            Map.entry(ObjectType.WATER, "resources/sprites/water.png")
    );

    private static final Map<ObjectType, Component> typeComponent = Map.ofEntries(
            Map.entry(ObjectType.BIGBLUE, new ecs.Components.nouns.IsBigBlue()),
            Map.entry(ObjectType.FLAG, new ecs.Components.nouns.IsFlag()),
            Map.entry(ObjectType.LAVA, new ecs.Components.nouns.IsLava()),
            Map.entry(ObjectType.ROCK, new ecs.Components.nouns.IsRock()),
            Map.entry(ObjectType.WALL, new ecs.Components.nouns.IsWall()),
            Map.entry(ObjectType.WATER, new ecs.Components.nouns.IsWater())
    );

    public static Entity create(ObjectType type, Vector3i position){
        var entity = new Entity();

        entity.add(new ecs.Components.Sprite(objectSprite.get(type)));
        entity.add(new ecs.Components.Position(position.x, position.y, position.z));
        entity.add(new ecs.Components.Object());

        if (typeComponent.get(type) != null)
            entity.add(typeComponent.get(type));

        entity.add(new ecs.Components.OriginalEntity(entity));
        return entity;
    }
}
