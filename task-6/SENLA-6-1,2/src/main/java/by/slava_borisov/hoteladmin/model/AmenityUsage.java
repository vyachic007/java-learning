package by.slava_borisov.hoteladmin.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class AmenityUsage extends Entity {
    private Amenity amenity;
    private int bookingId;
    private LocalDate usageDate;
    private int quantity;

    public AmenityUsage(int id, Amenity amenity, int bookingId, LocalDate usageDate, int quantity) {
        super(id);
        this.amenity = amenity;
        this.bookingId = bookingId;
        this.usageDate = usageDate;
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        if (amenity == null) {
            return 0.0;
        }
        return amenity.getPrice() * quantity;
    }

}
