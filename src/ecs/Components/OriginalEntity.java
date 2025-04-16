package ecs.Components;

import ecs.Entities.Entity;

public class OriginalEntity extends Component {
    Entity original;

    public OriginalEntity(Entity original) {
        this.original = new Entity();

        for(var component : original.getComponents().entrySet()){

            if (component.getValue() instanceof OriginalEntity)
                continue;

            this.original.add(component.getValue());
        }
    }
}
