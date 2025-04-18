package ecs.Components.objecttypes;

import ecs.Components.Component;

public class Wall extends Component {
    @Override
    public Component cloneComponent() {
        return new Wall();
    }
}
