package by.slava_borisov.tasks.task09_multithreading.senla09_3;

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
            System.out.println("[БУФЕР ПОЛОН] Producer ждет, размер: " + queue.size());
            wait();
        }
        queue.add(value);
        System.out.println("[Буфер: " + queue.size() + "/" + capacity + "]");
        notifyAll();
    }

    public synchronized int take() throws InterruptedException {
        while (queue.isEmpty()) {
            System.out.println("[БУФЕР ПУСТ] Consumer ждет");
            wait();
        }
        int v = queue.remove();
        notifyAll();
        return v;
    }
}
