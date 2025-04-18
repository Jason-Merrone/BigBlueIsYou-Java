package ecs.Components.objectattributes;

import ecs.Components.Component;
import ecs.Systems.System;

public class Push extends Component {

    @Override
    public Component cloneComponent() {
        return new Push();
    }
}
