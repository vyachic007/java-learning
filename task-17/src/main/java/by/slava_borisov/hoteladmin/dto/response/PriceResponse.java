package by.slava_borisov.hoteladmin.dto.response;

public record PriceResponse(
        Double totalPrice,
        Double pricePerNight,
        Long nights,
        String roomNumber
) { }