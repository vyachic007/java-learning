package by.slava_borisov.task1;

public class Task1ThreadStatesDemo {

    public static void main(String[] args) throws InterruptedException {

        Object lock = new Object();
        Object gate = new Object();
        boolean[] holderHasLock = { false };

        Thread worker = new Thread(() -> {
            long until = System.currentTimeMillis() + 600;
            while (System.currentTimeMillis() < until) {
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "worker");

        Thread holder = new Thread(() -> {
            synchronized (lock) {
                synchronized (gate) {
                    holderHasLock[0] = true;
                    gate.notifyAll();
                }
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "holder");

        System.out.println("NEW = " + worker.getState());

        worker.start();

        StateAwaiter.awaitState(worker, Thread.State.RUNNABLE, 1500);
        System.out.println("RUNNABLE = " + worker.getState());

        StateAwaiter.awaitState(worker, Thread.State.TIMED_WAITING, 1500);
        System.out.println("TIMED_WAITING = " + worker.getState());

        holder.start();
        synchronized (gate) {
            while (!holderHasLock[0]) {
                gate.wait();
            }
        }
        StateAwaiter.awaitState(worker, Thread.State.BLOCKED, 3000);
        System.out.println("BLOCKED = " + worker.getState());

        StateAwaiter.awaitState(worker, Thread.State.WAITING, 3000);
        System.out.println("WAITING = " + worker.getState());

        synchronized (lock) {
            lock.notifyAll();
        }
        worker.join();
        System.out.println("TERMINATED  = " + worker.getState());
    }
}
