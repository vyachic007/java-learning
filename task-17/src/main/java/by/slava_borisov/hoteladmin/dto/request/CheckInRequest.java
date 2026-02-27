package by.slava_borisov.hoteladmin.dto.request;

import by.slava_borisov.hoteladmin.dto.GuestDto;

import java.time.LocalDate;

public record CheckInRequest(
        GuestDto guest,
        Long roomId,
        LocalDate checkInDate,
        LocalDate checkOutDate
) { }
