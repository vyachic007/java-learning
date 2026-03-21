package by.slava_borisov.producer;

import by.slava_borisov.producer.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ProducerApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        System.out.println("Продюсер запущен. Генерация 5 сообщений в секунду...");

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}