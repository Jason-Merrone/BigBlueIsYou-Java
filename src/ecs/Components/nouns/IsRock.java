package ecs.Components.nouns;

import ecs.Components.Component;

public class IsRock extends Component {
    @Override
    public Component cloneComponent() {
        return new IsRock();
    }
}
