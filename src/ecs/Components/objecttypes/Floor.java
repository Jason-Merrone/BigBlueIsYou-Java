package ecs.Components.objecttypes;

import ecs.Components.Component;

public class Floor extends Component {
    @Override
    public Component cloneComponent() {
        return new Floor();
    }
}
