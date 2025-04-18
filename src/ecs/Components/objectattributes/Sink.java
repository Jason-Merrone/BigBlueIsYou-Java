package ecs.Components.objectattributes;

import ecs.Components.Component;

public class Sink extends Component {

    @Override
    public Component cloneComponent() {
        return new Sink();
    }
}
