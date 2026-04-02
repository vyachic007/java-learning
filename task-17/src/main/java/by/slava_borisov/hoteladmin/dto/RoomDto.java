package by.slava_borisov.hoteladmin.dto;

import java.math.BigDecimal;

public record RoomDto(
        Long id,
        Integer number,
        BigDecimal pricePerNight,
        RoomStatusDto status,
        int capacity,
        int stars
) { }