// Undo.java
package ecs.Systems;

import ecs.Entities.Entity;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class Undo extends System {
    private static class Snapshot {
        final Map<Long, Entity> undoEntities;
        final Map<Long, Entity> rendererEntities;
        final Map<Long, Entity> collisionEntities;
        final Map<Long, Entity> movementEntities;
        final Map<Long, Entity> keyboardEntities;

        Snapshot(Map<Long, Entity> u,
                 Map<Long, Entity> r,
                 Map<Long, Entity> c,
                 Map<Long, Entity> m,
                 Map<Long, Entity> k) {
            undoEntities      = u;
            rendererEntities  = r;
            collisionEntities = c;
            movementEntities  = m;
            keyboardEntities  = k;
        }
    }

    private final Deque<Snapshot> undoStack = new ArrayDeque<>();
    private final Renderer      sysRenderer;
    private final Collision     sysCollision;
    private final Movement      sysMovement;
    private final KeyboardInput sysKeyboard;

    public Undo(Renderer sysRenderer,
                Collision sysCollision,
                Movement sysMovement,
                KeyboardInput sysKeyboard) {
        super();
        this.sysRenderer  = sysRenderer;
        this.sysCollision = sysCollision;
        this.sysMovement  = sysMovement;
        this.sysKeyboard  = sysKeyboard;
    }

    @Override public void update(double dt) { }

    public void push() {
        // Clone every entity only *once* so that all systems share identical instances
        Map<Long, Entity> snapshot = deepCopy(sysRenderer.entities);

        undoStack.push(new Snapshot(
                new HashMap<>(snapshot),
                new HashMap<>(snapshot),
                new HashMap<>(snapshot),
                new HashMap<>(snapshot),
                new HashMap<>(snapshot)
        ));
    }

    public void pop() {
        if (undoStack.isEmpty()) return;
        Snapshot s = undoStack.pop();

        entities.clear();
        entities.putAll(s.undoEntities);

        sysRenderer.entities.clear();
        sysRenderer.entities.putAll(s.rendererEntities);

        sysCollision.entities.clear();
        sysCollision.entities.putAll(s.collisionEntities);

        sysMovement.entities.clear();
        sysMovement.entities.putAll(s.movementEntities);

        sysKeyboard.entities.clear();
        sysKeyboard.entities.putAll(s.keyboardEntities);
    }

    private Map<Long, Entity> deepCopy(Map<Long, Entity> orig) {
        Map<Long, Entity> copy = new HashMap<>();
        for (Entity e : orig.values()) {
            copy.put(e.getId(), new Entity(e));
        }
        return copy;
    }
}
