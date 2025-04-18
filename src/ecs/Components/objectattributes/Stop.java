package ecs.Components.objectattributes;

import ecs.Components.Component;

public class Stop extends Component {

    @Override
    public Component cloneComponent() {
        return new Stop();
    }
}
