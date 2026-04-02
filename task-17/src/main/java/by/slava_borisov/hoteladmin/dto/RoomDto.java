package by.slava_borisov.hoteladmin.dto;

public record RoomDto(
        Long id,
        Integer number,
        double pricePerNight,
        RoomStatusDto status,
        int capacity,
        int stars
) { }