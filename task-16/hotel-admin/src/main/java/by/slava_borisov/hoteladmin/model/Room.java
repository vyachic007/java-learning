package by.slava_borisov.hoteladmin.model;

import by.slava_borisov.hoteladmin.util.Messages;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "rooms")
public class Room extends BaseEntity {

    @Column(name = "number", nullable = false, unique = true)
    private String number;

    @Column(name = "price_per_night", nullable = false)
    private double pricePerNight;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RoomStatus status;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "stars", nullable = false)
    private int stars;

    @OneToMany(mappedBy = "room")
    private List<Booking> bookings = new ArrayList<>();


    public Room(String number, double pricePerNight, RoomStatus status,
                int capacity, int stars) {
        super();
        this.number = number;
        setPricePerNight(pricePerNight);
        this.status = status;
        this.capacity = capacity;
        this.stars = stars;
    }

    public Room(Long id, String number, int stars, int capacity,
                RoomStatus status, double pricePerNight) {
        super(id);
        this.number = number;
        this.stars = stars;
        this.capacity = capacity;
        this.status = status;
        setPricePerNight(pricePerNight);
    }


    public void setPricePerNight(double pricePerNight) {
        if (pricePerNight < 0) {
            throw new IllegalArgumentException(Messages.NOT_NEGATIVE_PRICE);
        }
        this.pricePerNight = pricePerNight;
    }

    public boolean isAvailable() {
        return status == RoomStatus.AVAILABLE;
    }
}
