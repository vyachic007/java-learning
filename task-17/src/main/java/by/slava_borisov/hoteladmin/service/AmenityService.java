package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.hoteladmin.dto.AmenityDto;
import by.slava_borisov.hoteladmin.dto.AmenityUsageDto;
import by.slava_borisov.hoteladmin.util.SortCriteria;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AmenityService {

    AmenityDto addAmenity(AmenityDto amenityDto);

    List<AmenityDto> getAllAmenities();

    void changeAmenityPrice(Long amenityId, BigDecimal newPrice);

    Optional<AmenityDto> findAmenityById(Long amenityId);

    List<AmenityDto> getAmenitiesSortedBy(SortCriteria criteria);

    AmenityUsageDto addAmenityToGuest(Long guestId, Long amenityId, LocalDate usageDate, int quantity);

    List<AmenityUsageDto> viewGuestAmenities(Long guestId);
}