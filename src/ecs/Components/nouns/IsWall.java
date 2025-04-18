package ecs.Components.nouns;

import ecs.Components.Component;

public class IsWall extends Component {
    @Override
    public Component cloneComponent() {
        return new IsWall();
    }
}
