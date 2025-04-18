package ecs.Components.nouns;

import ecs.Components.Component;

public class IsPush extends Component {
    @Override
    public Component cloneComponent() {
        return new IsPush();
    }
}
