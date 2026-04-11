package by.slava_borisov.hoteladmin.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "amenity_usages")
public class AmenityUsage extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "amenity_id", nullable = false)
    private Amenity amenity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(name = "usage_date", nullable = false)
    private LocalDate usageDate;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    public AmenityUsage(LocalDate usageDate, int quantity, Amenity amenity, Booking booking) {
        super();
        this.amenity = amenity;
        this.booking = booking;
        this.usageDate = usageDate;
        this.quantity = quantity;
    }

    public AmenityUsage(Long id, LocalDate usageDate, int quantity, Amenity amenity, Booking booking) {
        super(id);
        this.amenity = amenity;
        this.booking = booking;
        this.usageDate = usageDate;
        this.quantity = quantity;
    }
}
