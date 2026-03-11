package by.slava_borisov.tasks.task09_multithreading.senla09_3;

import java.util.Random;

public class Producer implements Runnable {

    private final BoundedBuffer buffer;
    private final Random random = new Random();

    public Producer(BoundedBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
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
