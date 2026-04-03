package by.slava_borisov.hoteladmin.controller.rest;

import by.slava_borisov.hoteladmin.dto.AmenityDto;
import by.slava_borisov.hoteladmin.dto.request.ChangePriceRequest;
import by.slava_borisov.hoteladmin.mapper.sort.AmenitySortMapper;
import by.slava_borisov.hoteladmin.service.AmenityService;
import by.slava_borisov.hoteladmin.util.SortCriteria;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/amenities")
@RequiredArgsConstructor
public class AmenityRestController {

    private final AmenityService amenityService;
    private final AmenitySortMapper amenitySortMapper;

    @GetMapping
    public ResponseEntity<List<AmenityDto>> getAmenities(
            @RequestParam(required = false) String sort
    ) {
        SortCriteria criteria = amenitySortMapper.map(sort);
        log.info("GET: getAmenities | sortCriteria={}", sort);

        return ResponseEntity.ok(amenityService.getAmenitiesSortedBy(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AmenityDto> getAmenityById(
            @PathVariable("id") @Positive Long id
    ) {
        log.info("GET: getAmenityById | amenityId={}", id);
        return ResponseEntity.ok(amenityService.getAmenityById(id));
    }

    @PostMapping
    public ResponseEntity<AmenityDto> addAmenity(
            @Valid @RequestBody AmenityDto amenityDto
    ) {
        AmenityDto created = amenityService.addAmenity(amenityDto);
        log.info("POST: addAmenity | id={}, name={}, price={}, category={}",
                created.id(), created.name(), created.price(), created.category());

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}/price")
    public ResponseEntity<Void> changeAmenityPrice(
            @PathVariable("id") @Positive Long id,
            @Valid @RequestBody ChangePriceRequest request
    ) {
        amenityService.changeAmenityPrice(id, request.newPrice());
        log.info("PUT: changeAmenityPrice | amenityId={}, newPrice={}", id, request.newPrice());

        return ResponseEntity.ok().build();
    }
}