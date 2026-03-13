package by.slava_borisov.hoteladmin.controller.rest;

import by.slava_borisov.hoteladmin.dto.RoomDto;
import by.slava_borisov.hoteladmin.service.QueryService;
import by.slava_borisov.hoteladmin.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportRestController {

    private final RoomService roomService;
    private final QueryService queryService;

    @GetMapping("/available-rooms-count")
    public int getAvailableRoomsCount() {
        int count =  queryService.countAvailableRooms();
        log.info("GET: getAvailableRoomsCount | count={}", count);

        return count;
    }

    @GetMapping("/guests-count")
    public int getGuestsCount() {
        int count =  queryService.countCurrentGuests();
        log.info("GET: getGuestsCount | count={}", count);

        return count;
    }

    @GetMapping("/available-rooms")
    public List<RoomDto> getAvailableRooms(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        log.info("GET: getAvailableRooms | date={}", date);

        return roomService.getAvailableRoomsOnDate(date);
    }
}
