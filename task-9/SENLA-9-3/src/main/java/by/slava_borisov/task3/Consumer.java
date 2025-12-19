package by.slava_borisov.task3;

public class Consumer implements Runnable {
    private final BoundedBuffer buffer;
    private final int n;

    public Consumer(BoundedBuffer buffer, int n) {
        this.buffer = buffer;
        this.n = n;
    }

    @Override
    public void run() {
        for (int i = 0; i < n; i++) {
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
