package by.slava_borisov.hoteladmin.controller.rest;

import by.slava_borisov.hoteladmin.dto.BookingDto;
import by.slava_borisov.hoteladmin.dto.PriceDto;
import by.slava_borisov.hoteladmin.dto.request.CheckInRequest;
import by.slava_borisov.hoteladmin.dto.request.CheckOutRequest;
import by.slava_borisov.hoteladmin.dto.request.PriceCalculationRequest;
import by.slava_borisov.hoteladmin.mapper.entity.BookingMapper;
import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.service.BookingService;
import by.slava_borisov.hoteladmin.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingRestController {

    private final BookingService bookingService;
    private final RoomService roomService;
    private final BookingMapper bookingMapper;

    @PostMapping("/check-in")
    public ResponseEntity<BookingDto> checkIn(
            @Valid @RequestBody CheckInRequest request
    ) {
        Booking booking = bookingService.checkIn(
                request.guestId(),
                request.roomId(),
                request.checkInDate(),
                request.checkOutDate()
        );
        BookingDto bookingDto = bookingMapper.toDto(booking);

        log.info("POST: checkIn | guestId={}, roomId={}, checkInDate={}, checkOutDate={}",
                request.guestId(), request.roomId(), request.checkInDate(), request.checkOutDate());

        return ResponseEntity.status(HttpStatus.CREATED).body(bookingDto);
    }

    @PostMapping("/check-out")
    public ResponseEntity<Void> checkOut(
            @Valid @RequestBody CheckOutRequest request
    ) {
        bookingService.checkOut(request.roomId());
        log.info("POST: checkOut | roomId={}", request.roomId());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/price")
    public ResponseEntity<PriceDto> calculatePrice(
            @Valid @RequestBody PriceCalculationRequest request
    ) {
        PriceDto price = roomService.calculateRoomPrice(
                request.roomId(),
                request.checkInDate(),
                request.checkOutDate()
        );

        log.info("POST: price | roomId={}, pricePerNight={}, nights={}, totalPrice={}",
                request.roomId(), price.pricePerNight(), price.nights(), price.totalPrice());

        return ResponseEntity.ok(price);
    }
}