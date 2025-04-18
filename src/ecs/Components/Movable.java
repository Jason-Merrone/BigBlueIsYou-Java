package ecs.Components;

public class Movable extends Component {
    @Override
    public Component cloneComponent() {
        return new Movable();
    }
}
