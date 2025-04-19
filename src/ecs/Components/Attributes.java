package ecs.Components;

import ecs.Entities.AttributesEnum;

public class Attributes extends Component {

    public ecs.Entities.AttributesEnum attribute = null;

    public Attributes() {
        this.attribute = null;
    }

    public Attributes(ecs.Entities.AttributesEnum attribute) {
        this.attribute = attribute;
    }

    @Override
    public Component cloneComponent() {
        Attributes clonedAttributes = new Attributes(this.attribute);
        return clonedAttributes;
    }

    public boolean hasAttribute(ecs.Entities.AttributesEnum attribute) {
        return this.attribute != null && this.attribute == attribute;
    }

    public void setAttribute(ecs.Entities.AttributesEnum attribute) {
        this.attribute = attribute;
    }

    public void clearAttribute() {
        this.attribute = null;
    }

    public ecs.Entities.AttributesEnum getAttribute() {
        return this.attribute;
    }
}