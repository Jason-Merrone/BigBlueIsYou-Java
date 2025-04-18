package ecs.Components.nouns;

import ecs.Components.Component;

public class IsFlag extends Component {
    @Override
    public Component cloneComponent() {
        return new IsFlag();
    }
}
