package by.slava_borisov.hoteladmin.controller.rest;

import by.slava_borisov.hoteladmin.dto.BookingDto;
import by.slava_borisov.hoteladmin.dto.RoomDto;
import by.slava_borisov.hoteladmin.dto.RoomStatusDto;
import by.slava_borisov.hoteladmin.dto.request.ChangePriceRequest;
import by.slava_borisov.hoteladmin.dto.request.RoomStatusRequest;
import by.slava_borisov.hoteladmin.exception.GlobalExceptionHandler;
import by.slava_borisov.hoteladmin.mapper.BookingMapper;
import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.model.RoomStatus;
import by.slava_borisov.hoteladmin.service.QueryService;
import by.slava_borisov.hoteladmin.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RoomRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoomService roomService;

    @Mock
    private QueryService queryService;

    @Mock
    private BookingMapper bookingMapper;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        RoomRestController controller = new RoomRestController(
                roomService,
                queryService,
                bookingMapper
        );

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }


    @Test
    void getAllRooms() throws Exception {
        List<RoomDto> rooms = List.of(
                new RoomDto(1L, "101", 100.0, RoomStatusDto.AVAILABLE, 2, 4),
                new RoomDto(2L, "102", 150.0, RoomStatusDto.OCCUPIED, 3, 5)
        );

        when(roomService.viewAllRoomsSortedBy(any())).thenReturn(rooms);

        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].number").value("101"))
                .andExpect(jsonPath("$[0].pricePerNight").value(100.0))
                .andExpect(jsonPath("$[0].status").value("AVAILABLE"))
                .andExpect(jsonPath("$[0].capacity").value(2))
                .andExpect(jsonPath("$[0].stars").value(4))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].number").value("102"))
                .andExpect(jsonPath("$[1].pricePerNight").value(150.0))
                .andExpect(jsonPath("$[1].status").value("OCCUPIED"))
                .andExpect(jsonPath("$[1].capacity").value(3))
                .andExpect(jsonPath("$[1].stars").value(5));

        verify(roomService).viewAllRoomsSortedBy(any());
    }

    @Test
    void getRoomById() throws Exception {
        RoomDto room = new RoomDto(1L, "101", 100.0, RoomStatusDto.AVAILABLE, 2, 4);

        when(roomService.findRoomById(1L)).thenReturn(Optional.of(room));

        mockMvc.perform(get("/api/rooms/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.number").value("101"))
                .andExpect(jsonPath("$.pricePerNight").value(100.0))
                .andExpect(jsonPath("$.status").value("AVAILABLE"))
                .andExpect(jsonPath("$.capacity").value(2))
                .andExpect(jsonPath("$.stars").value(4));

        verify(roomService).findRoomById(1L);
    }

    @Test
    void getRoomByIdShouldReturnNotFoundWhenRoomDoesNotExist() throws Exception {
        when(roomService.findRoomById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/rooms/999"))
                .andExpect(status().isNotFound());

        verify(roomService).findRoomById(999L);
    }

    @Test
    void getRoomByNumber() throws Exception {
        RoomDto room = new RoomDto(1L, "101", 100.0, RoomStatusDto.AVAILABLE, 2, 4);

        when(roomService.findRoomByNumber("101")).thenReturn(Optional.of(room));

        mockMvc.perform(get("/api/rooms/by-number")
                        .param("number", "101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.number").value("101"))
                .andExpect(jsonPath("$.pricePerNight").value(100.0))
                .andExpect(jsonPath("$.status").value("AVAILABLE"))
                .andExpect(jsonPath("$.capacity").value(2))
                .andExpect(jsonPath("$.stars").value(4));

        verify(roomService).findRoomByNumber("101");
    }

    @Test
    void getRoomByNumberShouldReturnNotFoundWhenRoomDoesNotExist() throws Exception {
        when(roomService.findRoomByNumber("999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/rooms/by-number")
                        .param("number", "999"))
                .andExpect(status().isNotFound());

        verify(roomService).findRoomByNumber("999");
    }

    @Test
    void getAvailableRooms() throws Exception {
        List<RoomDto> rooms = List.of(
                new RoomDto(1L, "101", 100.0, RoomStatusDto.AVAILABLE, 2, 4)
        );

        when(roomService.getAvailableRoomsOnDate(LocalDate.of(2026, 3, 26))).thenReturn(rooms);

        mockMvc.perform(get("/api/rooms/available")
                        .param("date", "2026-03-26"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].number").value("101"))
                .andExpect(jsonPath("$[0].pricePerNight").value(100.0))
                .andExpect(jsonPath("$[0].status").value("AVAILABLE"))
                .andExpect(jsonPath("$[0].capacity").value(2))
                .andExpect(jsonPath("$[0].stars").value(4));

        verify(roomService).getAvailableRoomsOnDate(LocalDate.of(2026, 3, 26));
    }

    @Test
    void addRoom() throws Exception {
        RoomDto request = new RoomDto(null, "101", 100.0, RoomStatusDto.AVAILABLE, 2, 4);
        RoomDto created = new RoomDto(1L, "101", 100.0, RoomStatusDto.AVAILABLE, 2, 4);

        when(roomService.addRoom(any(RoomDto.class))).thenReturn(created);

        mockMvc.perform(post("/api/rooms")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.number").value("101"))
                .andExpect(jsonPath("$.pricePerNight").value(100.0))
                .andExpect(jsonPath("$.status").value("AVAILABLE"))
                .andExpect(jsonPath("$.capacity").value(2))
                .andExpect(jsonPath("$.stars").value(4));

        verify(roomService).addRoom(any(RoomDto.class));
    }

    @Test
    void changeRoomPrice() throws Exception {
        ChangePriceRequest request = new ChangePriceRequest(150.0);

        doNothing().when(roomService).changeRoomPrice(1L, 150.0);

        mockMvc.perform(put("/api/rooms/1/price")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(roomService).changeRoomPrice(1L, 150.0);
    }

    @Test
    void changeRoomStatus() throws Exception {
        RoomStatusRequest request = new RoomStatusRequest(RoomStatus.OCCUPIED);

        doNothing().when(roomService).setRoomStatus(1L, RoomStatus.OCCUPIED);

        mockMvc.perform(put("/api/rooms/1/status")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(roomService).setRoomStatus(1L, RoomStatus.OCCUPIED);
    }

    @Test
    void getRoomBookings() throws Exception {
        Booking booking1 = new Booking();
        Booking booking2 = new Booking();

        BookingDto bookingDto1 = new BookingDto(
                1L,
                10L,
                1L,
                LocalDate.of(2026, 3, 20),
                LocalDate.of(2026, 3, 25),
                null
        );

        BookingDto bookingDto2 = new BookingDto(
                2L,
                11L,
                1L,
                LocalDate.of(2026, 3, 10),
                LocalDate.of(2026, 3, 15),
                LocalDate.of(2026, 3, 15)
        );

        when(queryService.getLastBookings(1L, 10)).thenReturn(List.of(booking1, booking2));
        when(bookingMapper.toDto(booking1)).thenReturn(bookingDto1);
        when(bookingMapper.toDto(booking2)).thenReturn(bookingDto2);

        mockMvc.perform(get("/api/rooms/1/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].guestId").value(10))
                .andExpect(jsonPath("$[0].roomId").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].guestId").value(11))
                .andExpect(jsonPath("$[1].roomId").value(1));

        verify(queryService).getLastBookings(1L, 10);
        verify(bookingMapper).toDto(booking1);
        verify(bookingMapper).toDto(booking2);
    }
}