package by.slava_borisov.hoteladmin.model;

import by.slava_borisov.hoteladmin.util.Messages;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Room {
    private int id;
    private String number;
    private double pricePerNight;
    private RoomStatus status;
    private Guest currentGuest;
    private int capacity;
    private int stars;
    private List<Booking> bookingHistory;


    public Room(int id, String number, double pricePerNight, RoomStatus status,
                Guest currentGuest, int capacity, int stars, List<Booking> bookingHistory) {
        this.id = id;
        this.number = number;
        this.pricePerNight = pricePerNight;
        this.status = status;
        this.currentGuest = currentGuest;
        this.capacity = capacity;
        this.stars = stars;
        this.bookingHistory = bookingHistory != null ? bookingHistory : new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public Guest getCurrentGuest() {
        return currentGuest;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setPricePerNight(double pricePerNight) {
        if (pricePerNight < 0) {
            throw new IllegalArgumentException(Messages.NOT_NEGATIVE_PRICE);
        }
        this.pricePerNight = pricePerNight;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getStars() {
        return stars;
    }

    public List<Booking> getBookingHistory() {
        return bookingHistory;
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

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public void assignGuest(Guest guest) {
        this.currentGuest = guest;
        this.status = RoomStatus.OCCUPIED;
    }

    public void removeGuest() {
        this.currentGuest = null;
        this.status = RoomStatus.AVAILABLE;
    }

}
