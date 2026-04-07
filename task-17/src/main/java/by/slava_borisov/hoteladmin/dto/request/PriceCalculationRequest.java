package by.slava_borisov.hoteladmin.dto.request;

import java.time.LocalDate;

public record PriceCalculationRequest(
        Long roomId,
        LocalDate checkInDate,
        LocalDate checkOutDate
) { }