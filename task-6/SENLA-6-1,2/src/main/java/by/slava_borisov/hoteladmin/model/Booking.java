package by.slava_borisov.hoteladmin.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Booking extends Entity {
    private Guest guest;
    private int roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDate actualCheckOutDate;
    private List<AmenityUsage> usedAmenities;


    public Booking(Guest guest, int roomId, LocalDate checkInDate,
                   LocalDate checkOutDate, List<AmenityUsage> usedAmenities) {
        super();
        this.guest = guest;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.usedAmenities = usedAmenities;
    }

    public Booking(int id, Guest guest, int roomId, LocalDate checkInDate,
                   LocalDate checkOutDate, LocalDate actualCheckOutDate, List<AmenityUsage> usedAmenities) {
        super(id);
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

}
