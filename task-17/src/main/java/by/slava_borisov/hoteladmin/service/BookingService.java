package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.model.AmenityUsage;

import java.time.LocalDate;

public interface BookingService {

    Booking checkIn(Guest guest, Long roomId, LocalDate checkInDate, LocalDate checkOutDate);

    void CheckOut(Long roomId);

    AmenityUsage addAmenityToGuest(Long guestId, Long amenityId, LocalDate usageDate, int quantity);
}