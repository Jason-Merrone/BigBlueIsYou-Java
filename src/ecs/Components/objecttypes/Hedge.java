package ecs.Components.objecttypes;

import ecs.Components.Component;

public class Hedge extends Component {
    @Override
    public Component cloneComponent() {
        return new Hedge();
    }
}
