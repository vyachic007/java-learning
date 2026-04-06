package by.slava_borisov.hoteladmin.controller.rest;

import by.slava_borisov.hoteladmin.dto.BookingDto;
import by.slava_borisov.hoteladmin.dto.RoomDto;
import by.slava_borisov.hoteladmin.dto.request.ChangePriceRequest;
import by.slava_borisov.hoteladmin.dto.request.RoomStatusRequest;
import by.slava_borisov.hoteladmin.mapper.entity.BookingMapper;
import by.slava_borisov.hoteladmin.mapper.sort.RoomSortMapper;
import by.slava_borisov.hoteladmin.service.QueryService;
import by.slava_borisov.hoteladmin.service.RoomService;
import by.slava_borisov.hoteladmin.util.SortCriteria;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

@Slf4j
@Validated
@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomRestController {

    private final RoomService roomService;
    private final QueryService queryService;
    private final BookingMapper bookingMapper;
    private final RoomSortMapper roomSortMapper;

    @GetMapping
    public ResponseEntity<List<RoomDto>> getAllRooms(
            @RequestParam(required = false) String sort
    ) {
        SortCriteria criteria = roomSortMapper.map(sort);
        log.info("GET: getAllRooms | sortCriteria={}", sort);

        return ResponseEntity.ok(roomService.getRoomsSortedBy(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> getRoomById(
            @PathVariable @Positive Long id
    ) {
        log.info("GET: getRoomById | roomId={}", id);

        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @GetMapping("/by-number")
    public ResponseEntity<RoomDto> getRoomByNumber(
            @RequestParam @Positive Integer number
    ) {
        log.info("GET: getRoomByNumber | roomNumber={}", number);

        return ResponseEntity.ok(roomService.getRoomByNumber(number));
    }

    @GetMapping("/available")
    public ResponseEntity<List<RoomDto>> getAvailableRooms(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        log.info("GET: getAvailableRooms | date={}", date);

        return ResponseEntity.ok(roomService.getAvailableRoomsOnDate(date));
    }

    @GetMapping("/available/count")
    public ResponseEntity<Integer> getAvailableRoomsCount() {
        int count = queryService.countAvailableRooms();
        log.info("GET: getAvailableRoomsCount | count={}", count);

        return ResponseEntity.ok(count);
    }

    @PostMapping
    public ResponseEntity<RoomDto> addRoom(
            @Valid @RequestBody RoomDto roomDto
    ) {
        RoomDto created = roomService.addRoom(roomDto);
        log.info("POST: addRoom | roomId={}, number={}, pricePerNight={}, status={}, capacity={}, stars={}",
                created.id(), created.number(), created.pricePerNight(), created.status(), created.capacity(), created.stars());

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}/price")
    public ResponseEntity<Void> changeRoomPrice(
            @PathVariable @Positive Long id,
            @Valid @RequestBody ChangePriceRequest request
    ) {
        log.info("PUT: changeRoomPrice | roomId={}, newPrice={}", id, request.newPrice());

        roomService.changeRoomPrice(id, request.newPrice());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> changeRoomStatus(
            @PathVariable @Positive Long id,
            @Valid @RequestBody RoomStatusRequest request
    ) {
        log.info("PUT: changeRoomStatus | roomId={}, newStatus={}", id, request.status());

        roomService.setRoomStatus(id, request.status());
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{id}/history")
    public ResponseEntity<List<BookingDto>> getRoomBookings(
            @PathVariable @Positive Long id,
            @RequestParam(defaultValue = "10") @Positive int limit
    ) {
        log.info("GET: getRoomBookings | roomId={}, limit={}", id, limit);

        List<BookingDto> result = queryService.getLastBookings(id, limit)
                .stream()
                .map(bookingMapper::toDto)
                .toList();

        return ResponseEntity.ok(result);
    }
}