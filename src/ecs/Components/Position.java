package ecs.Components;

import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.List;

public class Position extends Component {
    public int x, y, z;

    public Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public Component cloneComponent() {
        return new Position(x, y, z);
    }
}
