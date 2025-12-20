package by.slava_borisov.task3;

import java.util.ArrayDeque;
import java.util.Queue;

public class BoundedBuffer {
    private final int capacity;
    private final Queue<Integer> queue = new ArrayDeque<>();

    public BoundedBuffer(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void put(int value) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();
        }
        queue.add(value);
        notifyAll();
    }

    public synchronized int take() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        int v = queue.remove();
        notifyAll();
        return v;
    }
}
