package by.slava_borisov.hoteladmin.dto;

public record PriceDto(
        Double totalPrice,
        Double pricePerNight,
        Long nights,
        String roomNumber
) { }