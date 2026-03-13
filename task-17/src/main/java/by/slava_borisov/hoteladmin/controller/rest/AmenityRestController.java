package by.slava_borisov.hoteladmin.controller.rest;

import by.slava_borisov.hoteladmin.dto.AmenityDto;
import by.slava_borisov.hoteladmin.dto.request.ChangePriceRequest;
import by.slava_borisov.hoteladmin.exception.AmenityNotFoundException;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import by.slava_borisov.hoteladmin.util.SortCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/amenities")
@RequiredArgsConstructor
public class AmenityRestController {

    private final HotelFacade hotelFacade;

    @GetMapping
    public List<AmenityDto> getAllAmenities(
            @RequestParam(required = false) String sort
    ) {
        SortCriteria criteria = parseSortCriteria(sort);
        log.info("GET: getAllAmenities | sortCriteria={}", criteria);

        return hotelFacade.getAmenitiesSortedBy(criteria);
    }

    @GetMapping("/{id}")
    public AmenityDto getAmenityById(
            @PathVariable("id") Long id
    ) {
        log.info("GET: getAmenityById | amenityId={}", id);

        return hotelFacade.findAmenityById(id)
                .orElseThrow(() -> {
                    log.warn("Amenity not found: {}", id);
                    return new AmenityNotFoundException(id);
                });
    }

    @PostMapping
    public ResponseEntity<AmenityDto> addAmenity(
            @RequestBody AmenityDto amenityDto
    ) {
        AmenityDto created = hotelFacade.addAmenity(amenityDto);
        log.info("POST: addAmenity | id={}, name={}, price={}, category={}",
                created.id(), created.name(), created.price(), created.category());

        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}/price")
    public ResponseEntity<Void> changeAmenityPrice(
            @PathVariable Long id,
            @RequestBody ChangePriceRequest request
    ) {
        hotelFacade.changeAmenityPrice(id, request.newPrice());
        log.info("PUT: changeAmenityPrice | amenityId={}, newPrice={}",
                id, request.newPrice());

        return ResponseEntity.ok().build();
    }


    private SortCriteria parseSortCriteria(String sort) {
        if (sort == null) return SortCriteria.BY_ID;
        return switch (sort.toLowerCase()) {
            case "price" -> SortCriteria.BY_PRICE;
            case "category" -> SortCriteria.BY_NAME;
            default -> SortCriteria.BY_ID;
        };
    }
}
