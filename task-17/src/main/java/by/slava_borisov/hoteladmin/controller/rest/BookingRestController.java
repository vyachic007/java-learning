package by.slava_borisov.hoteladmin.controller.rest;

import by.slava_borisov.hoteladmin.dto.BookingDto;
import by.slava_borisov.hoteladmin.dto.request.CheckInRequest;
import by.slava_borisov.hoteladmin.dto.request.CheckOutRequest;
import by.slava_borisov.hoteladmin.dto.request.PriceCalculationRequest;
import by.slava_borisov.hoteladmin.dto.PriceDto;
import by.slava_borisov.hoteladmin.mapper.BookingMapper;
import by.slava_borisov.hoteladmin.mapper.GuestMapper;
import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.service.BookingService;
import by.slava_borisov.hoteladmin.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingRestController {

    private final BookingService bookingService;
    private final RoomService roomService;
    private final GuestMapper guestMapper;
    private final BookingMapper bookingMapper;

    @PostMapping("/check-in")
    public ResponseEntity<BookingDto> checkIn(
            @RequestBody CheckInRequest request
    ) {
        Guest guest = guestMapper.toEntity(request.guest());

        Booking booking = bookingService.checkIn(
                guest,
                request.roomId(),
                request.checkInDate(),
                request.checkOutDate()
        );
        BookingDto bookingDto = bookingMapper.toDto(booking);

        log.info("POST: checkIn | guestId={}, roomId={}, checkInDate={}, checkOutDate={}",
                request.guest().id(), request.roomId(), request.checkInDate(), request.checkOutDate());

        return ResponseEntity.status(201).body(bookingDto);
    }

    @PostMapping("/check-out")
    public ResponseEntity<?> checkOut(
            @RequestBody CheckOutRequest request
    ) {
        bookingService.checkOut(request.roomId());
        log.info("POST: isCheckOut | roomId={}", request.roomId());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/calculate-price")
    public ResponseEntity<PriceDto> calculatePrice(
            @RequestBody PriceCalculationRequest request
    ) {
        PriceDto price = roomService.calculateRoomPrice(
                request.roomId(),
                java.time.LocalDate.parse(request.checkInDate()),
                java.time.LocalDate.parse(request.checkOutDate())
        );

        log.info("POST: calculatePrice | roomId={}, pricePerNight={}, nights={}, totalPrice={}",
                request.roomId(), price.pricePerNight(), price.nights(), price.totalPrice());

        return ResponseEntity.ok(price);
    }
}
