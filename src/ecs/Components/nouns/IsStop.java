package ecs.Components.nouns;

import ecs.Components.Component;

public class IsStop extends Component {
    @Override
    public Component cloneComponent() {
        return new IsStop();
    }
}
