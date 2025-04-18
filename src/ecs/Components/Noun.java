package ecs.Components;

public class Noun extends Component{

    public ecs.Entities.Noun.NounType type;

    public Noun(ecs.Entities.Noun.NounType type){
        this.type = type;
    }
    @Override
    public Component cloneComponent() {
        return new Noun(type);
    }
}
