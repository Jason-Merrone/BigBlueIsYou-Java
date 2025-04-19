package ecs.Systems;

import ecs.Entities.Entity;
import ecs.Systems.collisionsystems.PushableCollision;
import ecs.Systems.collisionsystems.StoppedCollision;
import ecs.Systems.collisionsystems.WordCollision;
import ecs.Systems.collisionsystems.WinCollision;
import ecs.Systems.collisionsystems.KillCollision;
import ecs.Systems.collisionsystems.SinkCollision;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class Undo extends System {
    private final Deque<Map<Long, Entity>> undoStack = new ArrayDeque<>();

    private final Renderer          sysRenderer;
    private final PushableCollision sysCollision;
    private final StoppedCollision  sysStoppedCollision;
    private final WordCollision     sysWordCollision;
    private final WinCollision      sysWinCollision;
    private final KillCollision     sysKillCollision;
    private final SinkCollision     sysSinkCollision;
    private final Sentence          sysSentence;
    private final ApplyRules        sysApplyRules;
    private final Movement          sysMovement;
    private final KeyboardInput     sysKeyboard;

    public Undo(Renderer          sysRenderer,
                PushableCollision sysCollision,
                Movement          sysMovement,
                KeyboardInput     sysKeyboard,
                StoppedCollision  sysStoppedCollision,
                WordCollision     sysWordCollision,
                Sentence          sysSentence,
                ApplyRules        sysApplyRules,
                WinCollision      sysWinCollision,
                KillCollision     sysKillCollision,
                SinkCollision     sysSinkCollision
                ) {
        super();
        this.sysRenderer         = sysRenderer;
        this.sysCollision        = sysCollision;
        this.sysMovement         = sysMovement;
        this.sysKeyboard         = sysKeyboard;
        this.sysStoppedCollision = sysStoppedCollision;
        this.sysWordCollision    = sysWordCollision;
        this.sysWinCollision     = sysWinCollision;
        this.sysKillCollision    = sysKillCollision;
        this.sysSinkCollision    = sysSinkCollision;
        this.sysSentence         = sysSentence;
        this.sysApplyRules       = sysApplyRules;
    }

    @Override
    public void update(double dt) { }

    public void push() {
        undoStack.push(deepCopy(sysRenderer.entities));
    }

    public void pop() {
        if (undoStack.isEmpty()) return;
        Map<Long, Entity> snapshot = undoStack.pop();

        restore(this,                snapshot);
        restore(sysRenderer,         snapshot);
        restore(sysCollision,        snapshot);
        restore(sysStoppedCollision, snapshot);
        restore(sysWordCollision,    snapshot);
        restore(sysWinCollision,     snapshot);
        restore(sysKillCollision,    snapshot);
        restore(sysSinkCollision,    snapshot);
        restore(sysSentence,         snapshot);
        restore(sysApplyRules,       snapshot);
        restore(sysMovement,         snapshot);
        restore(sysKeyboard,         snapshot);
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
