package by.slava_borisov.hoteladmin.controller.rest;

import by.slava_borisov.hoteladmin.dto.BookingDto;
import by.slava_borisov.hoteladmin.dto.RoomDto;
import by.slava_borisov.hoteladmin.dto.request.ChangePriceRequest;
import by.slava_borisov.hoteladmin.dto.request.RoomStatusRequest;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import by.slava_borisov.hoteladmin.util.SortCriteria;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomRestController {

    private final HotelFacade hotelFacade;
    private static final Logger log = LoggerFactory.getLogger(RoomRestController.class);

    @GetMapping
    public List<RoomDto> getAllRooms(
            @RequestParam(required = false) String sort
    ) {
        SortCriteria criteria = getSortCriteria(sort);
        log.info("GET: getAllRooms | sortCriteria={}", sort);

        return hotelFacade.viewAllRoomsSortedBy(criteria);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> getRoomById(
            @PathVariable Long id
    ) {
        log.info("GET: getRoomById | roomId={}", id);

        return hotelFacade.findRoomById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-number")
    public ResponseEntity<RoomDto> getRoomByNumber(
            @RequestParam String number
    ) {
        log.info("GET: getRoomByNumber | roomNumber={}", number);

        return hotelFacade.findRoomByNumber(number)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/available")
    public List<RoomDto> getAvailableRooms(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        log.info("GET: getAvailableRooms | date={}", date);

        return hotelFacade.getAvailableRoomsOnDate(date);
    }

    @PostMapping
    public ResponseEntity<RoomDto> addRoom(
            @RequestBody RoomDto roomDto
    ) {
        RoomDto created = hotelFacade.addRoom(roomDto);
        log.info("POST: addRoom | roomId={}, number={}, pricePerNight={}, status={}, capacity={}, stars={}",
                created.id(), created.number(), created.pricePerNight(), created.status(), created.capacity(), created.stars());

        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}/price")
    public ResponseEntity<Void> changeRoomPrice(
            @PathVariable Long id,
            @RequestBody ChangePriceRequest request
    ) {
        log.info("PUT: changeRoomPrice | roomId={}, newPrice={}", id, request.newPrice());

        hotelFacade.changeRoomPrice(id, request.newPrice());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> changeRoomStatus(
            @PathVariable Long id,
            @RequestBody RoomStatusRequest request
    ) {
        log.info("PUT: changeRoomStatus | roomId={}, newStatus={}", id, request.status());

        hotelFacade.setRoomStatus(id, request.status());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/history")
    public List<BookingDto> getRoomBookings(@PathVariable Long id) {
        log.info("GET: getRoomBookings | roomId={}", id);

        return hotelFacade.viewRoomHistory(id);
    }

    private SortCriteria getSortCriteria(String sort) {
        if (sort == null) return SortCriteria.BY_ID;

        return switch (sort.toLowerCase()) {
            case "price" -> SortCriteria.BY_PRICE;
            case "capacity" -> SortCriteria.BY_CAPACITY;
            case "stars" -> SortCriteria.BY_STARS;
            default -> SortCriteria.BY_ID;
        };
    }
}
