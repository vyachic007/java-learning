package by.slava_borisov.hoteladmin.model;

import by.slava_borisov.hoteladmin.util.Messages;

public class Amenity {
    private int id;
    private String name;
    private double price;
    private String category;

    public Amenity(int id, String name, double price, String category) {
        this.id = id;
        this.name = name;
        setPrice(price);
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException(Messages.NOT_NEGATIVE_PRICE);
        }
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Наименование: %s, Цена: %f", id, name, price);
    }
}
