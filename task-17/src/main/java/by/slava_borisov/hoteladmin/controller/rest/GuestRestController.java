package by.slava_borisov.hoteladmin.controller.rest;

import by.slava_borisov.hoteladmin.dto.GuestDto;
import by.slava_borisov.hoteladmin.mapper.sort.GuestSortMapper;
import by.slava_borisov.hoteladmin.service.GuestService;
import by.slava_borisov.hoteladmin.service.QueryService;
import by.slava_borisov.hoteladmin.util.SortCriteria;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/guests")
@RequiredArgsConstructor
public class GuestRestController {

    private final GuestService guestService;
    private final QueryService queryService;
    private final GuestSortMapper guestSortMapper;

    @GetMapping
    public ResponseEntity<List<GuestDto>> getGuests(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String phone
    ) {
        if (phone != null && !phone.isBlank()) {
            log.info("GET: getGuests | phone={}", phone);
            return ResponseEntity.ok(List.of(guestService.getGuestByPhone(phone)));
        }

        SortCriteria criteria = guestSortMapper.map(sort);
        log.info("GET: getGuests | sortCriteria={}", sort);

        return ResponseEntity.ok(guestService.getGuestsSortedBy(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuestDto> getGuestById(
            @PathVariable("id") @Positive Long id
    ) {
        log.info("GET: getGuestById | guestId={}", id);
        return ResponseEntity.ok(guestService.getGuestById(id));
    }

    @GetMapping("/count/current")
    public ResponseEntity<Integer> getCurrentGuestsCount() {
        int count = queryService.countCurrentGuests();
        log.info("GET: getCurrentGuestsCount | count={}", count);

        return ResponseEntity.ok(count);
    }
}