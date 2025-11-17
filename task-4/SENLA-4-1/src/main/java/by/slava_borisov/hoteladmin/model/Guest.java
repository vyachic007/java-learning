package by.slava_borisov.hoteladmin.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Guest {
    private int id;
    private String fullName;
    private String phone;
    private int bookedRoomId;
    private List<Booking> bookingHistory;

    public Guest(int id, String fullName, String phone, List<Booking> bookingHistory) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.bookingHistory = bookingHistory != null ? bookingHistory : new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public List<Booking> getBookingHistory() {
        return bookingHistory;
   }

    public int getBookedRoomId() {
        return bookedRoomId;
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
        return bookingHistory.stream()
                .map(Booking::getUsedAmenities)
                .flatMap(List::stream)
                .collect(Collectors.toList());
   }

    public String getFullName() {
        return fullName;
    }

    public void setBookedRoomId(int bookedRoomId) {
        this.bookedRoomId = bookedRoomId;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, ФИО: '%s', Телефон:'%s', Забронированный номер: %d}",
                id, fullName, phone, bookedRoomId);
    }
}
