package by.slava_borisov.hoteladmin.dto.request;

public record PriceCalculationRequest(
        Long roomId,
        String checkInDate,
        String checkOutDate
) { }