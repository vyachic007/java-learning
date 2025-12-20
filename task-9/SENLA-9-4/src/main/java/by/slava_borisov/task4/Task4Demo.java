package by.slava_borisov.task4;

import java.time.LocalDateTime;

public class Task4Demo {
    public static void main(String[] args) {
        int seconds = 1;
        Thread timeThread = new Thread(new TimeThread(seconds));
        timeThread.start();
    }

    public static class TimeThread implements Runnable {
        private final int seconds;

        public TimeThread(int seconds) {
            if (seconds <= 0) {
                throw new IllegalArgumentException("Seconds must be positive");
            }
            this.seconds = seconds;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(seconds * 1000L);
                    System.out.println(LocalDateTime.now());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }
}
