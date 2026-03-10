package by.slava_borisov.task2;

import javax.swing.*;

public class Main {
    private static final Object lock = new Object();
    private static boolean isFirstThreadTurn = true;

    public static void main(String[] args) {
        Thread thread1 = new Thread(new RunnableTask("Поток1"));
        Thread thread2 = new Thread(new RunnableTask("Поток2"));


        thread1.start();
        thread2.start();
    }

    public static class RunnableTask implements Runnable {
        private final String name;

        public RunnableTask(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while ((name.equals("Поток1") && !isFirstThreadTurn) ||
                            (name.equals("Поток2") && isFirstThreadTurn)) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }

                    System.out.println(name + ": " + i);
                    isFirstThreadTurn = !isFirstThreadTurn;
                    lock.notifyAll();
                }
            }
        }
    }

}


