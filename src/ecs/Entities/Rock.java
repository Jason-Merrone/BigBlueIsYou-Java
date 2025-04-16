package ecs.Entities;

import org.joml.Vector3f;

public class Rock {

    public static Entity create(Vector3f position){
        var rock = new Entity();

        rock.add(new ecs.Components.Sprite("resources/sprites/rock.png"));
        rock.add(new ecs.Components.Position(position.x, position.y, position.z));
        rock.add(new ecs.Components.Object());

        rock.add(new ecs.Components.OriginalEntity(rock));
        return rock;
    }
}
