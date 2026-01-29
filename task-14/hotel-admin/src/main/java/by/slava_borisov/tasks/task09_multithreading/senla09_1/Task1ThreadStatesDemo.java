package by.slava_borisov.tasks.task09_multithreading.senla09_1;

public class Task1ThreadStatesDemo {

    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();

        Thread worker = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                //имитирую выполнение работы для удержания потока в состоянии RUNNABLE
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
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "holder");

        System.out.println("NEW = " + worker.getState());

        worker.start();
        awaitState(worker, Thread.State.RUNNABLE, 1000);
        System.out.println("RUNNABLE = " + worker.getState());

        awaitState(worker, Thread.State.TIMED_WAITING, 1000);
        System.out.println("TIMED_WAITING = " + worker.getState());

        holder.start();
        awaitState(worker, Thread.State.BLOCKED, 2000);
        System.out.println("BLOCKED = " + worker.getState());

        awaitState(worker, Thread.State.WAITING, 2000);
        System.out.println("WAITING = " + worker.getState());

        synchronized (lock) {
            lock.notifyAll();
        }
        worker.join();
        System.out.println("TERMINATED = " + worker.getState());
    }

    private static void awaitState(Thread t, Thread.State target, long timeoutMS) {
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
