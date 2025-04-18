package ecs.Components.nouns;

import ecs.Components.Component;

public class IsKill extends Component {
    @Override
    public Component cloneComponent() {
        return new IsKill()  ;
    }
}
