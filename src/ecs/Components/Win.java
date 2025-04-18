package ecs.Components;

public class Win extends Component {
    @Override
    public Component cloneComponent() {
        return new Win();
    }
}
