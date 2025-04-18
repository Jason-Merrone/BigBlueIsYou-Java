package ecs.Components.nouns;

import ecs.Components.Component;

public class IsWater extends Component {
    @Override
    public Component cloneComponent() {
        return new IsWater();
    }
}
