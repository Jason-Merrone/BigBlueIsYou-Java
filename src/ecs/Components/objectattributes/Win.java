package ecs.Components.objectattributes;

import ecs.Components.Component;

public class Win extends Component {

    @Override
    public Component cloneComponent() {
        return new Win();
    }
}
