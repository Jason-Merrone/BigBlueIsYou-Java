package ecs.Components.objecttypes;

import ecs.Components.Component;

public class Grass extends Component {
    @Override
    public Component cloneComponent() {
        return new Grass();
    }
}
