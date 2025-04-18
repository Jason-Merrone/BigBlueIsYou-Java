package ecs.Components.nouns;

import ecs.Components.Component;

public class IsYou extends Component {
    @Override
    public Component cloneComponent() {
        return new IsYou();
    }
}
