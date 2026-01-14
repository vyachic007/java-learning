package by.slava_borisov.hoteladmin.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class AmenityUsage extends Entity {
    private int amenityId;
    private int bookingId;
    private LocalDate usageDate;
    private int quantity;

    public AmenityUsage(int amenityId, int bookingId, LocalDate usageDate, int quantity) {
        super();
        this.amenityId = amenityId;
        this.bookingId = bookingId;
        this.usageDate = usageDate;
        this.quantity = quantity;
    }

    public AmenityUsage(int id, int amenityId, int bookingId, LocalDate usageDate, int quantity) {
        super(id);
        this.amenityId = amenityId;
        this.bookingId = bookingId;
        this.usageDate = usageDate;
        this.quantity = quantity;
    }
}
