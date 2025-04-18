package ecs.Components.objectattributes;

import ecs.Components.Component;

public class You extends Component {

    @Override
    public Component cloneComponent() {
        return new You();
    }
}
