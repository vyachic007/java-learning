package by.slava_borisov.task3;

public class Task3Demo {

    public static void main(String[] args) throws InterruptedException {
        BoundedBuffer buffer = new BoundedBuffer(5);
        int n = 30;

        Thread producer = new Thread(new Producer(buffer, n), "producer");
        Thread consumer = new Thread(new Consumer(buffer, n), "consumer");

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }
}
