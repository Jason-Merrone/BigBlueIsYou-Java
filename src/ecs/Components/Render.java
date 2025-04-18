package ecs.Components;

public class Render extends Component {
    @Override
    public Component cloneComponent() {
        return new Render();
    }
}
