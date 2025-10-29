import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Flower> bouquet = new ArrayList<>();

        bouquet.add(new Rose(5, 100));
        bouquet.add(new Tulip(6, 30.5));
        bouquet.add(new Chrysanthemum(2, 22.5));
        bouquet.add(new Lily(2, 15.0));

        System.out.println("===ВАШ БУКЕТ===");
        double totalCost = 0;
        for (Flower f : bouquet) {
            double flowerCost = f.calculatePrice();
            System.out.printf("%s: %d шт. x %.2f руб. = %.2f руб.\n",
                    f.getName(), f.getAmount(), f.getPrice(), flowerCost);
            totalCost += flowerCost;
        }

        System.out.println("\n=======================");
        System.out.printf("Итоговая цена %s рублей", totalCost);
    }
}
