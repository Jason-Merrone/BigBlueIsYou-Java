package ecs.Components;

public class IsUndoable extends Component{
    @Override
    public Component cloneComponent() {
        return new IsUndoable();
    }
}
