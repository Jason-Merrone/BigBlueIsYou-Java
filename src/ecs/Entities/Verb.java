package ecs.Entities;

import ecs.Components.Component;
import org.joml.Vector3i;

import java.util.Map;

public class Verb {
    public enum VerbType {
        IS
    }

    private static final Map<Verb.VerbType, String> verbSprite = Map.ofEntries(
            Map.entry(Verb.VerbType.IS, "resources/sprites/word-is.png")
    );

    private static final Map<Verb.VerbType, Component> typeComponent = Map.ofEntries(
            Map.entry(Verb.VerbType.IS, new ecs.Components.verbs.IsIsWord())
    );

    public static Entity create(Verb.VerbType type, Vector3i position){
        var entity = new Entity();

        entity.add(new ecs.Components.Sprite(verbSprite.get(type)));
        entity.add(new ecs.Components.Position(position.x, position.y, position.z));
        entity.add(new ecs.Components.Verb());
        entity.add(typeComponent.get(type));
        entity.add(new ecs.Components.nouns.IsPush());
        entity.add(new ecs.Components.Render());
        entity.add(new ecs.Components.IsUndoable());

        return entity;
    }
}
