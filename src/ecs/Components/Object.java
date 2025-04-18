package ecs.Components;

public class Object extends Component{

    public ecs.Entities.Object.ObjectType type;

    public Object(ecs.Entities.Object.ObjectType type){
        this.type = type;
    }

    @Override
    public Component cloneComponent() {
        return new Object(type);
    }
}
