package by.slava_borisov.hoteladmin.model;

import by.slava_borisov.hoteladmin.util.Messages;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "amenities")
public class Amenity extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "category", nullable = false)
    private String category;

    @OneToMany(mappedBy = "amenity")
    private List<AmenityUsage> usages = new ArrayList<>();


    public Amenity(String name, double price, String category) {
        super();
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public Amenity(Long id, String name, double price, String category) {
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
