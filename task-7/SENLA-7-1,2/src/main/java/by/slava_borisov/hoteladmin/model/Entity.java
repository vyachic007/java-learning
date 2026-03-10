package by.slava_borisov.hoteladmin.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

@Setter
@Getter
public abstract class Entity implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);
    protected int id;

    public Entity() {
        this.id = ID_GENERATOR.incrementAndGet();
    }

    public Entity(int id) {
        this.id = id;
        if (id >= ID_GENERATOR.get()) {
            ID_GENERATOR.set(id);
        }
    }

}
