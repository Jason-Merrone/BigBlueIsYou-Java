package ecs.Components.nouns;

import ecs.Components.Component;

public class IsSink extends Component {
    @Override
    public Component cloneComponent() {
        return new IsSink();
    }
}
