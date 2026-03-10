package by.slava_borisov.task3;

import java.util.Random;

public class Producer implements Runnable {
    private final BoundedBuffer buffer;
    private final int n;
    private final Random random = new Random();

    public Producer(BoundedBuffer buffer, int n) {
        this.buffer = buffer;
        this.n = n;
    }

    @Override
    public void run() {
        for (int i = 0; i < n; i++) {
            int value = random.nextInt(100);
            try {
                buffer.put(value);
                System.out.println(Thread.currentThread().getName() + " -> " + value);
                Thread.sleep(80);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
