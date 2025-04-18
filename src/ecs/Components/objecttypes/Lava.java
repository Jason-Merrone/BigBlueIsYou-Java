package ecs.Components.objecttypes;

import ecs.Components.Component;

public class Lava extends Component {
    @Override
    public Component cloneComponent() {
        return new Lava();
    }
}
