package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.hoteladmin.model.Booking;

import java.time.LocalDate;

public interface BookingService {

    Booking checkIn(Long guestId, Long roomId, LocalDate checkInDate, LocalDate checkOutDate);

    void checkOut(Long roomId);
}