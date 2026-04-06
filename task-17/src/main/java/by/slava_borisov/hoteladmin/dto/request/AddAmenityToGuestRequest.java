package by.slava_borisov.hoteladmin.dto.request;

import java.time.LocalDate;

public record AddAmenityToGuestRequest(
        Long guestId,
        Long amenityId,
        Integer quantity,
        LocalDate usageDate
) { }
