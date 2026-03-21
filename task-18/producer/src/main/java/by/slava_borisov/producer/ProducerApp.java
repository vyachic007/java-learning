package by.slava_borisov.producer;

import by.slava_borisov.producer.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ProducerApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        System.out.println("Продюсер запущен. Генерация 5 сообщений в секунду...");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Продюсер останавливается...");
            context.close();
        }));
    }
}