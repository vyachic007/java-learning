package by.slava_borisov.hoteladmin.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@EqualsAndHashCode(callSuper = true)
public class Booking extends Entity {
    private int guestId;
    private int roomId;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDate actualCheckOutDate;

    public Booking(int guestId, int roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        super();
        this.guestId = guestId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Booking(int id, int guestId, int roomId, LocalDate checkInDate,
                   LocalDate checkOutDate, LocalDate actualCheckOutDate) {
        super(id);
        this.guestId = guestId;
        this.roomId = roomId;
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
