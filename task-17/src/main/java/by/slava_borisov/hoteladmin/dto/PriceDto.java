package by.slava_borisov.hoteladmin.dto;

import java.math.BigDecimal;

public record PriceDto(
        BigDecimal totalPrice,
        BigDecimal pricePerNight,
        Long nights,
        Integer roomNumber
) { }