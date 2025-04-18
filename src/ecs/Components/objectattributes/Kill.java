package ecs.Components.objectattributes;

import ecs.Components.Component;

public class Kill extends Component {

    @Override
    public Component cloneComponent() {
        return new Kill();
    }
}
