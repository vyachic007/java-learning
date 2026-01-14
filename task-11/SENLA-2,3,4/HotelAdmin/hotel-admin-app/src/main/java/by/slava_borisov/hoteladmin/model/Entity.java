package by.slava_borisov.hoteladmin.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class Entity {

    protected int id;

    public Entity() {
    }

    public Entity(int id) {
        this.id = id;
    }

    public boolean isNew() {
        return id <= 0;
    }
}
