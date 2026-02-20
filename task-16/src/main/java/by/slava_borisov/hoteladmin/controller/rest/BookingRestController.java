package by.slava_borisov.hoteladmin.controller.rest;

import by.slava_borisov.hoteladmin.dto.BookingDto;
import by.slava_borisov.hoteladmin.dto.request.CheckInRequest;
import by.slava_borisov.hoteladmin.dto.request.CheckOutRequest;
import by.slava_borisov.hoteladmin.dto.request.PriceCalculationRequest;
import by.slava_borisov.hoteladmin.dto.response.PriceResponse;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingRestController {

    private final HotelFacade hotelFacade;

    @PostMapping("/check-in")
    public ResponseEntity<BookingDto> checkIn(
            @RequestBody CheckInRequest request
    ) {
        BookingDto bookingDto = hotelFacade.checkIn(
                request.guest(),
                request.roomId(),
                request.checkInDate(),
                request.checkOutDate()
        );

        return ResponseEntity.status(201).body(bookingDto);
    }

    @PostMapping("/check-out")
    public ResponseEntity<?> checkOut(@RequestBody CheckOutRequest request) {
        hotelFacade.checkOut(request.roomId());
        return ResponseEntity.ok().build();
    }


    @PostMapping("/calculate-price")
    public ResponseEntity<PriceResponse> calculatePrice(
            @RequestBody PriceCalculationRequest request
    ) {
        LocalDate checkIn = LocalDate.parse(request.checkInDate());
        LocalDate checkOut = LocalDate.parse(request.checkOutDate());

        PriceResponse price = hotelFacade.calculateRoomPrice(request.roomId(), checkIn, checkOut);
        return ResponseEntity.ok(price);
    }
}
