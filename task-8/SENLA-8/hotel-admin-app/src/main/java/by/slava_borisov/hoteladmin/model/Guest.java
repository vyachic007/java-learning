package by.slava_borisov.hoteladmin.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class Guest extends Entity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fullName;
    private String phone;
    private int bookedRoomId;
    private List<Booking> bookingHistory;
    private List<AmenityUsage> amenityUsages = new ArrayList<>();


    public Guest(String fullName, String phone, int bookedRoomId, List<Booking> bookingHistory) {
        super();
        this.fullName = fullName;
        this.phone = phone;
        this.bookedRoomId = bookedRoomId;
        this.bookingHistory = bookingHistory;
    }

    public Guest(int id, String fullName, String phone, int bookedRoomId, List<Booking> bookingHistory) {
        super(id);
        this.fullName = fullName;
        this.phone = phone;
        this.bookedRoomId = bookedRoomId;
        this.bookingHistory = bookingHistory;
    }

    public void addBooking(Booking booking) {
        bookingHistory.add(booking);
   }


    public Optional<Booking> getCurrentBooking() {
        return bookingHistory.stream()
                .filter(b -> b.isActive(LocalDate.now()))
                .findFirst();
   }

    public List<AmenityUsage> getAllAmenityUsages() {
        List<AmenityUsage> allUsages = new ArrayList<>(amenityUsages);
        allUsages.addAll(bookingHistory.stream()
                .map(Booking::getUsedAmenities)
                .flatMap(List::stream)
                .collect(Collectors.toList()));
        return allUsages;
    }


    public void addAmenityUsage(AmenityUsage amenityUsage) {
        this.amenityUsages.add(amenityUsage);
    }

}
