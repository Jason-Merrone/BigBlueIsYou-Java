package ecs.Components;

public class Verb extends Component{
    @Override
    public Component cloneComponent() {
        return new Verb();
    }
}
