package by.slava_borisov.hoteladmin.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@EqualsAndHashCode(callSuper = true)
public class Guest extends Entity {
    private String fullName;
    private String phone;

    private int bookedRoomId = 0;
    private List<Booking> bookingHistory = new ArrayList<>();

    public Guest(String fullName, String phone) {
        super();
        this.fullName = fullName;
        this.phone = phone;
    }

    public Guest(int id, String fullName, String phone) {
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
