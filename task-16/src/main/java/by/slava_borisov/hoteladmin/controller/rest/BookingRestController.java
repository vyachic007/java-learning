package by.slava_borisov.hoteladmin.controller.rest;

import by.slava_borisov.hoteladmin.dto.BookingDto;
import by.slava_borisov.hoteladmin.dto.request.CheckInRequest;
import by.slava_borisov.hoteladmin.dto.request.CheckOutRequest;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingRestController {

    private final HotelFacade hotelFacade;

    @PostMapping("/check-in")
    public BookingDto checkIn(
            @RequestBody CheckInRequest request
    ) {
        return hotelFacade.checkIn(
                request.guest(),
                request.roomId(),
                request.checkInDate(),
                request.checkOutDate()
        );
    }

    @PostMapping("/check-out")
    public void checkOut(
            @RequestBody CheckOutRequest request
    ) {
        hotelFacade.checkOut(request.roomId());
    }

    @GetMapping("/rooms/{roomId}/bookings")
    public List<BookingDto> getRoomBookings(
            @PathVariable Long roomId,
            @RequestParam(required = false) Integer limit
    ) {
        int historyLimit = limit != null ? limit : 10;
        return hotelFacade.viewRoomHistory(roomId, historyLimit);
    }
}
