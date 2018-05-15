package com.box2d.tutorial.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.box2d.tutorial.entity.components.TransformComponent;

import java.util.Comparator;

public class ZComparator implements Comparator<Entity> {

    private ComponentMapper<TransformComponent> cmTrans;

    public ZComparator() {
        cmTrans = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    public int compare(Entity entityA, Entity entityB) {
        float az = cmTrans.get(entityA).positon.z;
        float bz = cmTrans.get(entityB).positon.z;
        return Float.compare(az,bz);
    }
}
