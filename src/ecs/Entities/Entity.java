package ecs.Entities;

import ecs.Components.Component;

import java.util.*;
import java.util.stream.*;

/**
 * A named entity that contains a collection of Component instances
 */
public final class Entity {
    private final Map<Class<? extends Component>, Component> components = new HashMap<>();

    public Entity(Entity other) {
        this.id = other.id;            // preserve the same ID
        for (Component c : other.components.values()) {
            components.put(c.getClass(), c.cloneComponent());
        }
    }
    // and make sure you have a private setter for id so you don’t bump nextId:
    private long id;
    private static long nextId = 0;
    public Entity() {
        this.id = nextId++;
    }



    public long getId() {
        return id;
    }

    public void add(Component component) {
        Objects.requireNonNull(component, "components cannot be null");
        if (this.components.containsKey(component.getClass())) {
            throw new IllegalArgumentException("cannot add the same component twice");
        }

        this.components.put(component.getClass(), component);
    }

    // Get a copy of the components
    public Map<Class<? extends Component>, Component> getComponents() {
        return Collections.unmodifiableMap(this.components);
    }

    public <TComponent extends Component> void remove(Class<TComponent> type) {
        this.components.remove(type);
    }

    public <TComponent extends Component> boolean contains(Class<TComponent> type) {
        return components.containsKey(type) && components.get(type) != null;
    }

    public <TComponent extends Component> TComponent get(Class<TComponent> type) {
        if (!components.containsKey(type)) {
            throw new IllegalArgumentException(String.format("component of type %s is not a part of this entity", type.getName()));
        }
        // The use of generic to define TComponent is motivated by this code.  The use
        // of Class<? extends Component> won't return the actual component type, instead
        // it only returns Component, but we need the actual component type instead.
        return type.cast(this.components.get(type));
    }

    public void clear() {
        components.clear();
    }

    public void isStop(){
        if(this.contains(ecs.Components.Movable.class))
            this.remove(ecs.Components.Movable.class);

        if(!this.contains(ecs.Components.Collidable.class))
            this.add(new ecs.Components.Collidable());

        if(this.contains(ecs.Components.nouns.IsPush.class))
            this.remove(ecs.Components.nouns.IsPush.class);

        if(!this.contains(ecs.Components.nouns.IsStop.class))
            this.add(new ecs.Components.nouns.IsStop());
    }

    public void isPush(){
        if(!this.contains(ecs.Components.Movable.class))
            this.add(new ecs.Components.Movable());

        if(!this.contains(ecs.Components.Collidable.class))
            this.add(new ecs.Components.Collidable());

        if(!this.contains(ecs.Components.nouns.IsPush.class))
            this.add(new ecs.Components.nouns.IsPush());

        if(this.contains(ecs.Components.nouns.IsStop.class))
            this.remove(ecs.Components.nouns.IsStop.class);
    }

    public void isYou(){
        if(!this.contains(ecs.Components.Movable.class))
            this.add(new ecs.Components.Movable());

        if(!this.contains(ecs.Components.Collidable.class))
            this.add(new ecs.Components.Collidable());

        if(!this.contains(ecs.Components.nouns.IsYou.class))
            this.add(new ecs.Components.nouns.IsYou());

        if(this.contains(ecs.Components.nouns.IsStop.class))
            this.remove(ecs.Components.nouns.IsStop.class);
    }

    @Override
    public String toString() {
        return String.format("%d: %s", id, components.values().stream().map(c -> c.getClass().getSimpleName()).collect(Collectors.joining(", ")));
    }
}
