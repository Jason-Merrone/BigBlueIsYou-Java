package ecs.Components.objecttypes;

import ecs.Components.Component;

public class Flowers extends Component {
    @Override
    public Component cloneComponent() {
        return new Flowers();
    }
}
