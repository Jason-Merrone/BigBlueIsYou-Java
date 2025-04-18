package ecs.Components.objecttypes;

import ecs.Components.Component;

public class Rock extends Component {
    @Override
    public Component cloneComponent() {
        return new Rock();
    }
}
