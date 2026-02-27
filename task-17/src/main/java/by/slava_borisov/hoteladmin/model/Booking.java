package by.slava_borisov.hoteladmin.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "guest_id", nullable = false)
    private Guest guest;

    @ManyToOne(optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;

    @Column(name = "actual_check_out_date")
    private LocalDate actualCheckOutDate;

    public Booking(Guest guest, Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        super();
        this.guest = guest;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Booking(Long id, Guest guest, Room room, LocalDate checkInDate,
                   LocalDate checkOutDate, LocalDate actualCheckOutDate) {
        super(id);
        this.guest = guest;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.actualCheckOutDate = actualCheckOutDate;
    }

    private boolean isBetweenDates(LocalDate date) {
        if (date == null || checkInDate == null || checkOutDate == null) {
            return false;
        }
        return (date.equals(checkInDate) || date.isAfter(checkInDate))
                && date.isBefore(checkOutDate);
    }

    public boolean isActive(LocalDate date) {
        return isBetweenDates(date);
    }

    public long getStayDuration() {
        if (checkInDate == null) return 0;
        LocalDate exitDate = (actualCheckOutDate != null) ? actualCheckOutDate : LocalDate.now();
        return ChronoUnit.DAYS.between(checkInDate, exitDate);
    }
}
