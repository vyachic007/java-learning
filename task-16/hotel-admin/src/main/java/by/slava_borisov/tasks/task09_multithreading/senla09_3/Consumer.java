package by.slava_borisov.tasks.task09_multithreading.senla09_3;

public class Consumer implements Runnable {

    private final BoundedBuffer buffer;

    public Consumer(BoundedBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                int value = buffer.take();
                System.out.println(Thread.currentThread().getName() + " <- " + value);
                Thread.sleep(120);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
