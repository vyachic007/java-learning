package by.slava_borisov.hoteladmin.controller.rest;

import by.slava_borisov.hoteladmin.dto.RoomDto;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(ReportRestController.class);

    @GetMapping("/available-rooms-count")
    public int getAvailableRoomsCount() {
        int count =  hotelFacade.getAvailableRoomsCount();
        log.info("GET: getAvailableRoomsCount | count={}", count);

        return count;
    }

    @GetMapping("/guests-count")
    public int getGuestsCount() {
        int count =  hotelFacade.getGuestsCount();
        log.info("GET: getGuestsCount | count={}", count);

        return count;
    }

    @GetMapping("/available-rooms")
    public List<RoomDto> getAvailableRooms(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        log.info("GET: getAvailableRooms | date={}", date);

        return hotelFacade.getAvailableRoomsOnDate(date);
    }
}
