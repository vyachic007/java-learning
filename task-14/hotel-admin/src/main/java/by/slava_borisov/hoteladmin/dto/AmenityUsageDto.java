package by.slava_borisov.hoteladmin.dto;

import java.time.LocalDate;

public record AmenityUsageDto(
        Long id,
        Long amenityId,
        Long bookingId,
        LocalDate usageDate,
        int quantity
) {}
