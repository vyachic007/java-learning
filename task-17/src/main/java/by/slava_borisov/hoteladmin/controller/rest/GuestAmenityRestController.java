package by.slava_borisov.hoteladmin.controller.rest;

import by.slava_borisov.hoteladmin.dto.AmenityUsageDto;
import by.slava_borisov.hoteladmin.dto.request.AddAmenityToGuestRequest;
import by.slava_borisov.hoteladmin.service.AmenityService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/guests/{guestId}/amenities")
@RequiredArgsConstructor
public class GuestAmenityRestController {

    private final AmenityService amenityService;

    @GetMapping
    public ResponseEntity<List<AmenityUsageDto>> getGuestAmenities(
            @PathVariable("guestId") @Positive Long guestId
    ) {
        log.info("GET: getGuestAmenities | guestId={}", guestId);
        return ResponseEntity.ok(amenityService.getGuestAmenities(guestId));
    }


    @PostMapping
    public ResponseEntity<AmenityUsageDto> addAmenityToGuest(
            @PathVariable("guestId") @Positive Long guestId,
            @Valid @RequestBody AddAmenityToGuestRequest request
    ) {
        AmenityUsageDto result = amenityService.addAmenityToGuest(
                guestId,
                request.amenityId(),
                request.usageDate(),
                request.quantity()
        );

        log.info("POST: addAmenityToGuest | guestId={}, amenityId={}, quantity={}, usageDate={}",
                guestId, request.amenityId(), request.quantity(), request.usageDate());

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}