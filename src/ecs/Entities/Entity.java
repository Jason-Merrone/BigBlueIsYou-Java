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

        return type.cast(this.components.get(type));
    }

    public void clear() {
        components.clear();
    }

    @Override
    public String toString() {
        return String.format("%d: %s", id, components.values().stream().map(c -> c.getClass().getSimpleName()).collect(Collectors.joining(", ")));
    }
}
