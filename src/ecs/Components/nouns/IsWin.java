package ecs.Components.nouns;

import ecs.Components.Component;

public class IsWin extends Component {
    @Override
    public Component cloneComponent() {
        return new IsWin();
    }
}
