package by.slava_borisov.hoteladmin.dto;

import java.time.LocalDate;

public record BookingDto(
        Long id,
        Long guestId,
        Long roomId,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        LocalDate actualCheckOutDate
) {}
