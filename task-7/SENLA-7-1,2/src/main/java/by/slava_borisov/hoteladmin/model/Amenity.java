package by.slava_borisov.hoteladmin.model;

import by.slava_borisov.hoteladmin.util.Messages;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class Amenity extends Entity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private double price;
    private String category;

    public Amenity(String name, double price, String category) {
        super();
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public Amenity(int id, String name, double price, String category) {
        super(id);
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException(Messages.NOT_NEGATIVE_PRICE);
        }
        this.price = price;
    }
}
