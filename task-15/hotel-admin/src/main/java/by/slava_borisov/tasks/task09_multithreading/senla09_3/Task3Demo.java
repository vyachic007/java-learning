package by.slava_borisov.tasks.task09_multithreading.senla09_3;

public class Task3Demo {

    public static void main(String[] args) throws InterruptedException {
        int bufferCapacity = 5;
        long runDurationMillis = 5000;

        BoundedBuffer sharedBuffer = new BoundedBuffer(bufferCapacity);

        Thread producerThread = new Thread(new Producer(sharedBuffer), "Producer");
        Thread consumerThread = new Thread(new Consumer(sharedBuffer), "Consumer");

        producerThread.start();
        consumerThread.start();

        Thread.sleep(runDurationMillis);

        producerThread.interrupt();
        consumerThread.interrupt();

        producerThread.join();
        consumerThread.join();

        System.out.println("Программа завершена");
    }
}
