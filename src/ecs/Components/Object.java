package ecs.Components;

public class Object extends Component{
    @Override
    public Component cloneComponent() {
        return new Object();
    }
}
