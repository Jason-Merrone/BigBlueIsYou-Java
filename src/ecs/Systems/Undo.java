package ecs.Systems;

import ecs.Entities.Entity;
import ecs.Systems.collisionsystems.PushableCollision;
import ecs.Systems.collisionsystems.StoppedCollision;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class Undo extends System {
    private final Deque<Map<Long, Entity>> undoStack = new ArrayDeque<>();

    private final Renderer          sysRenderer;
    private final PushableCollision sysCollision;
    private final StoppedCollision  sysStoppedCollision;
    private final Movement          sysMovement;
    private final KeyboardInput     sysKeyboard;

    public Undo(Renderer          sysRenderer,
                PushableCollision sysCollision,
                Movement          sysMovement,
                KeyboardInput     sysKeyboard,
                StoppedCollision  sysStoppedCollision) {
        super();
        this.sysRenderer        = sysRenderer;
        this.sysCollision       = sysCollision;
        this.sysStoppedCollision = sysStoppedCollision;
        this.sysMovement        = sysMovement;
        this.sysKeyboard        = sysKeyboard;
    }

    @Override
    public void update(double dt) { }

    public void push() {
        undoStack.push(deepCopy(sysRenderer.entities));
    }

    public void pop() {
        if (undoStack.isEmpty()) return;
        Map<Long, Entity> snapshot = undoStack.pop();

        restore(this,              snapshot);
        restore(sysRenderer,       snapshot);
        restore(sysCollision,      snapshot);
        restore(sysStoppedCollision, snapshot);
        restore(sysMovement,       snapshot);
        restore(sysKeyboard,       snapshot);
    }

    private void restore(System system, Map<Long, Entity> snapshot) {
        system.entities.clear();
        for (Entity e : snapshot.values()) {
            system.add(e);
        }
    }

    private Map<Long, Entity> deepCopy(Map<Long, Entity> orig) {
        Map<Long, Entity> copy = new HashMap<>();
        for (Entity e : orig.values()) {
            copy.put(e.getId(), new Entity(e));
        }
        return copy;
    }
}
