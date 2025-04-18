package ecs.Systems;

import ecs.Entities.Entity;

public class Collision extends System {

    public Collision() {
        super(ecs.Components.Collidable.class);
    }

    @Override
    public void update(double elapsedTime) {
    }

    public Entity collidesWith(ecs.Entities.Entity entity) {
        var pos = entity.get(ecs.Components.Position.class);
        for(ecs.Entities.Entity other : entities.values()){
            var pos2 = other.get(ecs.Components.Position.class);
            if(pos.x == pos2.x && pos.y == pos2.y && entity != other){
                return other;
            }
        }
        return null;
    }
}
