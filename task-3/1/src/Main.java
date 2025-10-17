import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random rand = new Random();
        int num = rand.nextInt(100, 999);
        System.out.println("Натурально число: " + num);

        int first = num % 10;
        int second = (num / 10) % 10;
        int third = num / 100;

        System.out.println("Максимальный элемент: " + Math.max(first, Math.max(second, third)));
    }
}
