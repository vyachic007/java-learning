package by.slava_borisov.hoteladmin.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Booking {
    private int id;
    private Guest guest;
    private int roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDate actualCheckOutDate;
    private List<AmenityUsage> usedAmenities;


    public Booking(int id, Guest guest, int roomId, LocalDate checkInDate, LocalDate checkOutDate,
                   LocalDate actualCheckOutDate, List<AmenityUsage> usedAmenities) {
        this.id = id;
        this.guest = guest;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.actualCheckOutDate = actualCheckOutDate;
        this.usedAmenities = usedAmenities;
    }

    private boolean isBetweenDates(LocalDate date) {
        return (date.equals(checkInDate) || date.isAfter(checkInDate)) && (date.equals(checkOutDate) || date.isBefore(checkOutDate));
    }


    public boolean isActive(LocalDate date) {
        return isBetweenDates(date);
    }

    public boolean overlapsWith(LocalDate date) {
        return isBetweenDates(date);
    }

    public long getStayDuration() {
        LocalDate exitDate = actualCheckOutDate != null ? actualCheckOutDate : LocalDate.now();
        return ChronoUnit.DAYS.between(checkInDate, exitDate);
    }

    public void addAmenityUsage(AmenityUsage amenityUsage) {
        if (usedAmenities == null) {
            usedAmenities = new ArrayList<>();
        }
        usedAmenities.add(amenityUsage);
    }

    public List<AmenityUsage> getUsedAmenities() {
        return usedAmenities;
    }

    public void setActualCheckOutDate(LocalDate actualCheckOutDate) {
        this.actualCheckOutDate = actualCheckOutDate;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public LocalDate getActualCheckOutDate() {
        return actualCheckOutDate;
    }

    public int getRoomId() {
        return roomId;
    }

    public Guest getGuest() {
        return guest;
    }

    public int getId() {
        return id;
    }
}
