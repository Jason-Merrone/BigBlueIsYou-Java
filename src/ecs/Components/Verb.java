package ecs.Components;

public class Verb extends Component{

    public ecs.Entities.Verb.VerbType type;

    public Verb(ecs.Entities.Verb.VerbType type){
        this.type = type;
    }

    @Override
    public Component cloneComponent() {
        return new Verb(type);
    }
}
