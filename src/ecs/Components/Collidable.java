package ecs.Components;

public class Collidable extends Component {
    @Override
    public Component cloneComponent() {
        return new Collidable();
    }
}
