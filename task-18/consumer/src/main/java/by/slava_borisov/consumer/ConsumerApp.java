package by.slava_borisov.consumer;

import by.slava_borisov.consumer.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ConsumerApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        System.out.println("Consumer запущен. Ожидание сообщений...");

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}