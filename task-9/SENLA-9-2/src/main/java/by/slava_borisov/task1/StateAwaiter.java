package by.slava_borisov.task1;

public class StateAwaiter {
    public static void awaitState(Thread t, Thread.State target, long timeoutMS) {
        long deadline = System.currentTimeMillis() + timeoutMS;

        while (t.getState() != target && System.currentTimeMillis() < deadline) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}