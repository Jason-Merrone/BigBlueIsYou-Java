package ecs.Components;

import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.List;

public class Position extends Component {
    public List<Vector3i> segments = new ArrayList<>();

    public Position(int x, int y, int z) {
        segments.add(new Vector3i(x, y, z));
    }

    public int getX() {
        return segments.getFirst().x;
    }

    public int getY() {
        return segments.getFirst().y;
    }

    public int getZ() {
        return segments.getFirst().z;
    }
}
