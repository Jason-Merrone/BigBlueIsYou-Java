package ecs.Components.objecttypes;

import ecs.Components.Component;

public class Water extends Component {
    @Override
    public Component cloneComponent() {
        return new Water();
    }
}
