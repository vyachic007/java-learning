package by.slava_borisov.hoteladmin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "rooms")
public class Room extends BaseEntity {

    @Column(name = "number", nullable = false, unique = true)
    private Integer number;

    @Column(name = "price_per_night", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RoomStatus status;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "stars", nullable = false)
    private int stars;

    @OneToMany(mappedBy = "room")
    private List<Booking> bookings = new ArrayList<>();
}