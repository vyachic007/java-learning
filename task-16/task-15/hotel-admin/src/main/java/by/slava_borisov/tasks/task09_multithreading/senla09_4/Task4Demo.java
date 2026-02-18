package by.slava_borisov.tasks.task09_multithreading.senla09_4;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task4Demo {

    public static void main(String[] args) throws InterruptedException {
        int intervalSeconds = 1;

        Thread timeThread = new Thread(new TimeThread(intervalSeconds), "TimeThread");
        timeThread.setDaemon(true);
        timeThread.start();

        Thread.sleep(5000);
    }

    public static class TimeThread implements Runnable {
        private final int intervalSeconds;
        private static final DateTimeFormatter FORMATTER =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        public TimeThread(int intervalSeconds) {
            if (intervalSeconds <= 0) {
                throw new IllegalArgumentException("Интервал должен быть положительным");
            }
            this.intervalSeconds = intervalSeconds;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(intervalSeconds * 1000L);
                    System.out.println("Системное время: " + LocalDateTime.now().format(FORMATTER));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }
}
