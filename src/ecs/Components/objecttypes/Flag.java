package ecs.Components.objecttypes;

import ecs.Components.Component;

public class Flag extends Component {
    @Override
    public Component cloneComponent() {
        return new Flag();
    }
}
