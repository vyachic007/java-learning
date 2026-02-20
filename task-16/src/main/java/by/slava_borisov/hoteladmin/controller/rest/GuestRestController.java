package by.slava_borisov.hoteladmin.controller.rest;

import by.slava_borisov.hoteladmin.dto.AmenityUsageDto;
import by.slava_borisov.hoteladmin.dto.GuestDto;
import by.slava_borisov.hoteladmin.dto.request.AddAmenityToGuestRequest;
import by.slava_borisov.hoteladmin.exception.GuestNotFoundException;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import by.slava_borisov.hoteladmin.util.SortCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
@RequiredArgsConstructor
public class GuestRestController {

    private final HotelFacade hotelFacade;


    @GetMapping
    public List<GuestDto> getAllGuests(
            @RequestParam(required = false) String sort
    ) {
        SortCriteria criteria = parseSortCriteria(sort);
        return hotelFacade.viewGuestsSortedBy(criteria);
    }

    @GetMapping("/{id}")
    public GuestDto getGuestById(
            @PathVariable("id") Long id
    ) {
        return hotelFacade.findGuestById(id)
                .orElseThrow(() -> new GuestNotFoundException(id));
    }

    @GetMapping("/by-phone")
    public GuestDto getGuestByPhone(
            @RequestParam String phone
    ) {
        return hotelFacade.findGuestByPhone(phone)
                .orElseThrow(() -> new GuestNotFoundException(phone));
    }

    @GetMapping("/{id}/amenities")
    public List<AmenityUsageDto> getGuestAmenities(
            @PathVariable Long id
    ) {
        return hotelFacade.viewGuestAmenities(id);
    }

    @PostMapping("/{id}/amenities")
    public ResponseEntity<AmenityUsageDto> addAmenityToGuest(
            @PathVariable("id") Long id,
            @RequestBody AddAmenityToGuestRequest request
    ) {
        AmenityUsageDto result = hotelFacade.addAmenityToGuest(
                id,
                request.amenityId(),
                request.usageDate(),
                request.quantity()
        );
        return ResponseEntity.status(201).body(result);
    }

    private SortCriteria parseSortCriteria(String sort) {
        if (sort == null) return SortCriteria.BY_CHECK_OUT_DATE;
        return switch (sort.toLowerCase()) {
            case "date" -> SortCriteria.BY_CHECK_OUT_DATE;
            case "id" -> SortCriteria.BY_ID;
            default -> SortCriteria.BY_NAME;
        };
    }
}
