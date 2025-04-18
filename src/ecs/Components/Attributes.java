package ecs.Components;

import java.util.Arrays;
import java.util.HashSet;

public class Attributes extends Component {

    private HashSet<ecs.Entities.AttributesEnum> attributes = new HashSet<>();

    public Attributes(ecs.Entities.AttributesEnum... attributes){
        this.attributes.addAll(Arrays.asList(attributes));
    }

    @Override
    public Component cloneComponent() {
        Attributes clonedAttributes = new Attributes();
        clonedAttributes.attributes.addAll(this.attributes);
        return clonedAttributes;
    }

    public boolean hasAttribute(ecs.Entities.AttributesEnum attribute) {
        return attributes.contains(attribute);
    }

    public void addAttribute(ecs.Entities.AttributesEnum attribute) {
        attributes.add(attribute);
    }

    public void removeAttribute(ecs.Entities.AttributesEnum attribute) {
        attributes.remove(attribute);
    }
}
