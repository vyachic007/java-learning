package by.slava_borisov.hoteladmin.model;

import by.slava_borisov.hoteladmin.util.Messages;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Room  extends Entity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String number;
    private double pricePerNight;
    private RoomStatus status;
    private Guest currentGuest;
    private int capacity;
    private int stars;
    private List<Booking> bookingHistory;


    public Room(String number, double pricePerNight, RoomStatus status,
                Guest currentGuest, int capacity, int stars, List<Booking> bookingHistory) {
        super();
        this.number = number;
        this.pricePerNight = pricePerNight;
        this.status = status;
        this.currentGuest = currentGuest;
        this.capacity = capacity;
        this.stars = stars;
        this.bookingHistory = bookingHistory;
    }

    public Room(int id, String number, List<Booking> bookingHistory, int stars,
                int capacity, Guest currentGuest, RoomStatus status, double pricePerNight) {
        super(id);
        this.number = number;
        this.bookingHistory = bookingHistory;
        this.stars = stars;
        this.capacity = capacity;
        this.currentGuest = currentGuest;
        this.status = status;
        this.pricePerNight = pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        if (pricePerNight < 0) {
            throw new IllegalArgumentException(Messages.NOT_NEGATIVE_PRICE);
        }
        this.pricePerNight = pricePerNight;
    }

    public void addToBookingHistory(Booking booking) {
        bookingHistory.add(booking);
    }

    public boolean isAvailableOnDate(LocalDate date) {
        if (status != RoomStatus.AVAILABLE) {
            return false;
        }

        if (bookingHistory == null) {
            return true;
        }

        for (Booking booking : bookingHistory) {
            LocalDate checkIn = booking.getCheckInDate();
            LocalDate checkOut = booking.getCheckOutDate();
            if ((date.equals(checkIn) || date.isAfter(checkIn)) && date.isBefore(checkOut)) {
                return false;
            }
        }
        return true;
    }


    public boolean isAvailable() {
        return status == RoomStatus.AVAILABLE;
    }

    public void removeGuest() {
        this.currentGuest = null;
        this.status = RoomStatus.AVAILABLE;
    }

}
