package by.slava_borisov.tasks.task09_multithreading.senla09_2;

public class Task2Demo {

    private static final int ITERATIONS = 10;
    private static boolean isFirstTurn = true;

    public static void main(String[] args) {
        Thread thread1 = new Thread(new RunnableTask("Поток1", true));
        Thread thread2 = new Thread(new RunnableTask("Поток2", false));

        thread1.start();
        thread2.start();
    }

    public static class RunnableTask implements Runnable {
        private final String name;
        private final boolean isFirst;

        public RunnableTask(String name, boolean isFirst) {
            this.name = name;
            this.isFirst = isFirst;
        }

        @Override
        public void run() {
            for (int i = 0; i < ITERATIONS; i++) {
                synchronized (Task2Demo.class) {
                    while (isFirst != isFirstTurn) {
                        try {
                            Task2Demo.class.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }

                    System.out.println(name + ": " + i);
                    isFirstTurn = !isFirstTurn;
                    Task2Demo.class.notifyAll();
                }
            }
        }
    }
}
