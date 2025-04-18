package ecs.Components.nouns;

import ecs.Components.Component;

public class IsLava extends Component {
    @Override
    public Component cloneComponent() {
        return new IsLava();
    }
}
