package by.slava_borisov.hoteladmin.controller.rest;

import by.slava_borisov.hoteladmin.dto.BookingDto;
import by.slava_borisov.hoteladmin.dto.request.CheckOutRequest;
import by.slava_borisov.hoteladmin.dto.request.PriceCalculationRequest;
import by.slava_borisov.hoteladmin.dto.response.PriceResponse;
import by.slava_borisov.hoteladmin.exception.GlobalExceptionHandler;
import by.slava_borisov.hoteladmin.mapper.BookingMapper;
import by.slava_borisov.hoteladmin.mapper.GuestMapper;
import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.service.BookingService;
import by.slava_borisov.hoteladmin.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookingRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @Mock
    private RoomService roomService;

    @Mock
    private GuestMapper guestMapper;

    @Mock
    private BookingMapper bookingMapper;

    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        BookingRestController controller = new BookingRestController(
                bookingService,
                roomService,
                guestMapper,
                bookingMapper
        );

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }


    @Test
    void checkIn() throws Exception {
        String requestJson = """
            {
              "guest": {
                "id": 1,
                "fullName": "Иван Иванов",
                "phone": "+375291111111"
              },
              "roomId": 2,
              "checkInDate": "2026-03-26",
              "checkOutDate": "2026-03-30"
            }
            """;

        Guest guest = new Guest();
        Booking booking = new Booking();
        BookingDto bookingDto = new BookingDto(
                10L,
                1L,
                2L,
                LocalDate.of(2026, 3, 26),
                LocalDate.of(2026, 3, 30),
                null
        );

        when(guestMapper.toEntity(any())).thenReturn(guest);
        when(bookingService.checkIn(
                eq(guest),
                eq(2L),
                eq(LocalDate.of(2026, 3, 26)),
                eq(LocalDate.of(2026, 3, 30))
        )).thenReturn(booking);
        when(bookingMapper.toDto(booking)).thenReturn(bookingDto);

        mockMvc.perform(post("/api/bookings/check-in")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.guestId").value(1))
                .andExpect(jsonPath("$.roomId").value(2))
                .andExpect(jsonPath("$.checkInDate[0]").value(2026))
                .andExpect(jsonPath("$.checkInDate[1]").value(3))
                .andExpect(jsonPath("$.checkInDate[2]").value(26))
                .andExpect(jsonPath("$.checkOutDate[0]").value(2026))
                .andExpect(jsonPath("$.checkOutDate[1]").value(3))
                .andExpect(jsonPath("$.checkOutDate[2]").value(30));

        verify(guestMapper).toEntity(any());
        verify(bookingService).checkIn(
                guest,
                2L,
                LocalDate.of(2026, 3, 26),
                LocalDate.of(2026, 3, 30)
        );
        verify(bookingMapper).toDto(booking);
    }


    @Test
    void checkInShouldReturnInternalServerErrorWhenServiceThrowsException() throws Exception {
        String requestJson = """
                {
                  "guest": {
                    "id": 1,
                    "fullName": "Иван Иванов",
                    "phone": "+375291111111"
                  },
                  "roomId": 2,
                  "checkInDate": "2026-03-26",
                  "checkOutDate": "2026-03-30"
                }
                """;

        Guest guest = new Guest();

        when(guestMapper.toEntity(any())).thenReturn(guest);
        when(bookingService.checkIn(
                eq(guest),
                eq(2L),
                eq(LocalDate.of(2026, 3, 26)),
                eq(LocalDate.of(2026, 3, 30))
        )).thenThrow(new RuntimeException());

        mockMvc.perform(post("/api/bookings/check-in")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void checkOut() throws Exception {
        CheckOutRequest request = new CheckOutRequest(2L);

        doNothing().when(bookingService).checkOut(2L);

        mockMvc.perform(post("/api/bookings/check-out")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(bookingService).checkOut(2L);
    }

    @Test
    void checkOutShouldReturnInternalServerErrorWhenServiceThrowsException() throws Exception {
        CheckOutRequest request = new CheckOutRequest(2L);

        Mockito.doThrow(new RuntimeException())
                .when(bookingService).checkOut(2L);

        mockMvc.perform(post("/api/bookings/check-out")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());

        verify(bookingService).checkOut(2L);
    }

    @Test
    void calculatePrice() throws Exception {
        PriceCalculationRequest request = new PriceCalculationRequest(
                2L,
                "2026-03-26",
                "2026-03-30"
        );

        PriceResponse response = new PriceResponse(
                400.0,
                100.0,
                4L,
                "101"
        );

        when(roomService.calculateRoomPrice(2L, "2026-03-26", "2026-03-30"))
                .thenReturn(response);

        mockMvc.perform(post("/api/bookings/calculate-price")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value(400.0))
                .andExpect(jsonPath("$.pricePerNight").value(100.0))
                .andExpect(jsonPath("$.nights").value(4))
                .andExpect(jsonPath("$.roomNumber").value("101"));

        verify(roomService).calculateRoomPrice(2L, "2026-03-26", "2026-03-30");
    }

    @Test
    void calculatePriceShouldReturnInternalServerErrorWhenServiceThrowsException() throws Exception {
        PriceCalculationRequest request = new PriceCalculationRequest(
                2L,
                "2026-03-26",
                "2026-03-30"
        );

        when(roomService.calculateRoomPrice(2L, "2026-03-26", "2026-03-30"))
                .thenThrow(new RuntimeException());

        mockMvc.perform(post("/api/bookings/calculate-price")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());

        verify(roomService).calculateRoomPrice(2L, "2026-03-26", "2026-03-30");
    }
}