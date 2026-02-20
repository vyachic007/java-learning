package by.slava_borisov.hoteladmin.controller.rest;

import by.slava_borisov.hoteladmin.dto.BookingDto;
import by.slava_borisov.hoteladmin.dto.request.CheckInRequest;
import by.slava_borisov.hoteladmin.dto.request.CheckOutRequest;
import by.slava_borisov.hoteladmin.service.HotelFacade;
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

    @GetMapping("/rooms/{roomId}/bookings")
    public List<BookingDto> getRoomBookings(
            @PathVariable Long roomId,
            @RequestParam(required = false) Integer limit
    ) {
        int historyLimit = limit != null ? limit : 2;
        System.out.println("🔍 getRoomBookings roomId=" + roomId + ", limit=" + historyLimit);
        return hotelFacade.viewRoomHistory(roomId, historyLimit);
    }
}
