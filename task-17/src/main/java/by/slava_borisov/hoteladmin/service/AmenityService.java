package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.hoteladmin.dto.AmenityDto;
import by.slava_borisov.hoteladmin.dto.AmenityUsageDto;
import by.slava_borisov.hoteladmin.util.SortCriteria;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface AmenityService {

    AmenityDto addAmenity(AmenityDto amenityDto);

    List<AmenityDto> getAmenitiesSortedBy(SortCriteria criteria);

    AmenityDto getAmenityById(Long amenityId);

    void changeAmenityPrice(Long amenityId, BigDecimal newPrice);

    AmenityUsageDto addAmenityToGuest(Long guestId, Long amenityId, LocalDate usageDate, int quantity);

    List<AmenityUsageDto> getGuestAmenities(Long guestId);
}