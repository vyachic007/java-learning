package by.slava_borisov.hoteladmin.controller.rest;

import by.slava_borisov.hoteladmin.dto.RoomDto;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportRestController {

    private final HotelFacade hotelFacade;

    @GetMapping("/available-rooms-count")
    public int getAvailableRoomsCount() {
        return hotelFacade.getAvailableRoomsCount();
    }

    @GetMapping("/guests-count")
    public int getGuestsCount() {
        return hotelFacade.getGuestsCount();
    }

    @GetMapping("/available-rooms")
    public List<RoomDto> getAvailableRooms(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return hotelFacade.getAvailableRoomsOnDate(date);
    }
}
