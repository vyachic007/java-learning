package by.slava_borisov.hoteladmin.model;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Entity {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static void resetIdGenerator() {
        ID_GENERATOR.set(0);
    }

    public static int getCurrentMaxId() {
        return ID_GENERATOR.get();
    }
}
