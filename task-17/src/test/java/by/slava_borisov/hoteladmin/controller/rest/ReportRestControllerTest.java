package by.slava_borisov.hoteladmin.controller.rest;

import by.slava_borisov.hoteladmin.dto.RoomDto;
import by.slava_borisov.hoteladmin.dto.RoomStatusDto;
import by.slava_borisov.hoteladmin.exception.GlobalExceptionHandler;
import by.slava_borisov.hoteladmin.service.QueryService;
import by.slava_borisov.hoteladmin.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ReportRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoomService roomService;

    @Mock
    private QueryService queryService;

    @BeforeEach
    void setUp() {
        ReportRestController controller = new ReportRestController(roomService, queryService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }


    @Test
    void getAvailableRoomsCount() throws Exception {
        when(queryService.countAvailableRooms()).thenReturn(5);

        mockMvc.perform(get("/api/reports/available-rooms-count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(queryService).countAvailableRooms();
    }

    @Test
    void getAvailableRoomsCountShouldReturnInternalServerError() throws Exception {
        when(queryService.countAvailableRooms()).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/reports/available-rooms-count"))
                .andExpect(status().isInternalServerError());

        verify(queryService).countAvailableRooms();
    }

    @Test
    void getGuestsCount() throws Exception {
        when(queryService.countCurrentGuests()).thenReturn(10);

        mockMvc.perform(get("/api/reports/guests-count"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));

        verify(queryService).countCurrentGuests();
    }

    @Test
    void getGuestsCountShouldReturnInternalServerError() throws Exception {
        when(queryService.countCurrentGuests()).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/reports/guests-count"))
                .andExpect(status().isInternalServerError());

        verify(queryService).countCurrentGuests();
    }


    @Test
    void getAvailableRooms() throws Exception {
        List<RoomDto> rooms = List.of(
                new RoomDto(
                        1L,
                        "101",
                        100.0,
                        RoomStatusDto.AVAILABLE,
                        2,
                        4
                ),
                new RoomDto(
                        2L,
                        "102",
                        150.0,
                        RoomStatusDto.AVAILABLE,
                        3,
                        5
                )
        );

        when(roomService.getAvailableRoomsOnDate(LocalDate.of(2026, 3, 26))).thenReturn(rooms);

        mockMvc.perform(get("/api/reports/available-rooms")
                        .param("date", "2026-03-26"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].number").value("101"))
                .andExpect(jsonPath("$[0].capacity").value(2))
                .andExpect(jsonPath("$[0].pricePerNight").value(100.0))
                .andExpect(jsonPath("$[0].stars").value(4))
                .andExpect(jsonPath("$[0].status").value("AVAILABLE"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].number").value("102"))
                .andExpect(jsonPath("$[1].capacity").value(3))
                .andExpect(jsonPath("$[1].pricePerNight").value(150.0))
                .andExpect(jsonPath("$[1].stars").value(5))
                .andExpect(jsonPath("$[1].status").value("AVAILABLE"));

        verify(roomService).getAvailableRoomsOnDate(LocalDate.of(2026, 3, 26));
    }

    @Test
    void getAvailableRoomsShouldReturnInternalServerError() throws Exception {
        when(roomService.getAvailableRoomsOnDate(LocalDate.of(2026, 3, 26)))
                .thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/reports/available-rooms")
                        .param("date", "2026-03-26"))
                .andExpect(status().isInternalServerError());

        verify(roomService).getAvailableRoomsOnDate(LocalDate.of(2026, 3, 26));
    }
}