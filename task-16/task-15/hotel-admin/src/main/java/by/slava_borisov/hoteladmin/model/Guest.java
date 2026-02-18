package by.slava_borisov.hoteladmin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "guests")
public class Guest extends BaseEntity {

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone", nullable = false)
    private String phone;

    @OneToMany(mappedBy = "guest")
    private List<Booking> bookingHistory = new ArrayList<>();

    public Guest(String fullName, String phone) {
        super();
        this.fullName = fullName;
        this.phone = phone;
    }

    public Guest(Long id, String fullName, String phone) {
        super(id);
        this.fullName = fullName;
        this.phone = phone;
    }

    public void addBooking(Booking booking) {
        if (bookingHistory == null) {
            bookingHistory = new ArrayList<>();
        }
        bookingHistory.add(booking);
    }

    public Optional<Booking> getCurrentBooking() {
        if (bookingHistory == null) {
            return Optional.empty();
        }
        LocalDate today = LocalDate.now();
        return bookingHistory.stream()
                .filter(b -> b != null && b.isActive(today))
                .findFirst();
    }
}
