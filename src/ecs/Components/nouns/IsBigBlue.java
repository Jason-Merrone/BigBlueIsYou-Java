package ecs.Components.nouns;

import ecs.Components.Component;

public class IsBigBlue extends Component {
    @Override
    public Component cloneComponent() {
        return new IsBigBlue();
    }
}
