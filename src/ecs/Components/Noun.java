package ecs.Components;

public class Noun extends Component{
    @Override
    public Component cloneComponent() {
        return new Noun();
    }
}
