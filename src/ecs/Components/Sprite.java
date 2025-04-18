package ecs.Components;

public class Sprite extends Component{
    public String location;

    public Sprite(String spriteLocation){
        this.location = spriteLocation;
    }

    @Override
    public Component cloneComponent() {
        return new Sprite(this.location);
    }
}
