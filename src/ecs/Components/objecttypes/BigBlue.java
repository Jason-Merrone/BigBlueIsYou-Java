package ecs.Components.objecttypes;

import ecs.Components.Component;

public class BigBlue extends Component {
    @Override
    public Component cloneComponent() {
        return new BigBlue();
    }
}
