package by.slava_borisov.hoteladmin.controller.rest;

import by.slava_borisov.hoteladmin.dto.AmenityUsageDto;
import by.slava_borisov.hoteladmin.dto.GuestDto;
import by.slava_borisov.hoteladmin.dto.request.AddAmenityToGuestRequest;
import by.slava_borisov.hoteladmin.exception.AmenityNotFoundException;
import by.slava_borisov.hoteladmin.exception.BookingNotFoundException;
import by.slava_borisov.hoteladmin.exception.GlobalExceptionHandler;
import by.slava_borisov.hoteladmin.exception.GuestNotFoundException;
import by.slava_borisov.hoteladmin.service.AmenityService;
import by.slava_borisov.hoteladmin.service.GuestService;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GuestRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GuestService guestService;

    @Mock
    private AmenityService amenityService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        GuestRestController controller = new GuestRestController(guestService, amenityService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllGuests() throws Exception {
        List<GuestDto> guests = List.of(
                new GuestDto(1L, "Иванов Иван Иванович", "123456789"),
                new GuestDto(2L, "Александр Сергеевич Пушкин", "987654321")
        );

        when(guestService.viewGuestsSortedBy(any())).thenReturn(guests);

        mockMvc.perform(get("/api/guests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].fullName").value("Иванов Иван Иванович"))
                .andExpect(jsonPath("$[0].phone").value("123456789"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].fullName").value("Александр Сергеевич Пушкин"))
                .andExpect(jsonPath("$[1].phone").value("987654321"));

        verify(guestService).viewGuestsSortedBy(any());
    }

    @Test
    void getGuestById() throws Exception {
        GuestDto guest = new GuestDto(1L, "Иванов Иван Иванович", "1233456789");

        when(guestService.findGuestById(1L)).thenReturn(Optional.of(guest));
        mockMvc.perform(get("/api/guests/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("Иванов Иван Иванович"))
                .andExpect(jsonPath("$.phone").value("1233456789"));

        verify(guestService).findGuestById(1L);
    }

    @Test
    void getGuestByPhone() throws Exception {
        GuestDto guest = new GuestDto(1L, "Иванов Иван Иванович", "1233456789");

        when(guestService.findGuestByPhone("1233456789")).thenReturn(Optional.of(guest));

        mockMvc.perform(get("/api/guests/by-phone")
                        .param("phone", "1233456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("Иванов Иван Иванович"))
                .andExpect(jsonPath("$.phone").value("1233456789"));

        verify(guestService).findGuestByPhone("1233456789");
    }

    @Test
    void getGuestAmenities() throws Exception {
        List<AmenityUsageDto> amenities = List.of(
                new AmenityUsageDto(
                        1L,
                        10L,
                        100L,
                        LocalDate.of(2026, 3, 26),
                        2
                ),
                new AmenityUsageDto(
                        2L,
                        11L,
                        100L,
                        LocalDate.of(2026, 3, 27),
                        1
                )
        );

        when(amenityService.viewGuestAmenities(1L)).thenReturn(amenities);

        mockMvc.perform(get("/api/guests/1/amenities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].amenityId").value(10))
                .andExpect(jsonPath("$[0].bookingId").value(100))
                .andExpect(jsonPath("$[0].usageDate[0]").value(2026))
                .andExpect(jsonPath("$[0].usageDate[1]").value(3))
                .andExpect(jsonPath("$[0].usageDate[2]").value(26))
                .andExpect(jsonPath("$[0].quantity").value(2))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].amenityId").value(11))
                .andExpect(jsonPath("$[1].bookingId").value(100))
                .andExpect(jsonPath("$[1].usageDate[0]").value(2026))
                .andExpect(jsonPath("$[1].usageDate[1]").value(3))
                .andExpect(jsonPath("$[1].usageDate[2]").value(27))
                .andExpect(jsonPath("$[1].quantity").value(1));

        verify(amenityService).viewGuestAmenities(1L);
    }

    @Test
    void getGuestAmenitiesShouldReturnNotFoundWhenGuestDoesNotExist() throws Exception {
        when(amenityService.viewGuestAmenities(999L))
                .thenThrow(new GuestNotFoundException(999L));

        mockMvc.perform(get("/api/guests/999/amenities"))
                .andExpect(status().isNotFound());

        verify(amenityService).viewGuestAmenities(999L);
    }

    @Test
    void addAmenityToGuest() throws Exception {
        AddAmenityToGuestRequest request = new AddAmenityToGuestRequest(
                1L,
                10L,
                2,
                LocalDate.of(2026, 3, 26)
        );

        AmenityUsageDto result = new AmenityUsageDto(
                1L,
                10L,
                100L,
                LocalDate.of(2026, 3, 26),
                2
        );

        when(amenityService.addAmenityToGuest(
                1L,
                10L,
                LocalDate.of(2026, 3, 26),
                2
        )).thenReturn(result);

        mockMvc.perform(post("/api/guests/1/amenities")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amenityId").value(10))
                .andExpect(jsonPath("$.bookingId").value(100))
                .andExpect(jsonPath("$.usageDate[0]").value(2026))
                .andExpect(jsonPath("$.usageDate[1]").value(3))
                .andExpect(jsonPath("$.usageDate[2]").value(26))
                .andExpect(jsonPath("$.quantity").value(2));

        verify(amenityService).addAmenityToGuest(
                1L,
                10L,
                LocalDate.of(2026, 3, 26),
                2
        );
    }

    @Test
    void addAmenityToGuestShouldReturnNotFoundWhenGuestDoesNotExist() throws Exception {
        AddAmenityToGuestRequest request = new AddAmenityToGuestRequest(
                999L,
                10L,
                2,
                LocalDate.of(2026, 3, 26)
        );

        when(amenityService.addAmenityToGuest(
                999L,
                10L,
                LocalDate.of(2026, 3, 26),
                2
        )).thenThrow(new GuestNotFoundException(999L));

        mockMvc.perform(post("/api/guests/999/amenities")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(amenityService).addAmenityToGuest(
                999L,
                10L,
                LocalDate.of(2026, 3, 26),
                2
        );
    }

    @Test
    void addAmenityToGuestShouldReturnNotFoundWhenAmenityDoesNotExist() throws Exception {
        AddAmenityToGuestRequest request = new AddAmenityToGuestRequest(
                1L,
                999L,
                2,
                LocalDate.of(2026, 3, 26)
        );

        when(amenityService.addAmenityToGuest(
                1L,
                999L,
                LocalDate.of(2026, 3, 26),
                2
        )).thenThrow(new AmenityNotFoundException(999L));

        mockMvc.perform(post("/api/guests/1/amenities")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(amenityService).addAmenityToGuest(
                1L,
                999L,
                LocalDate.of(2026, 3, 26),
                2
        );
    }

    @Test
    void addAmenityToGuestShouldReturnNotFoundWhenBookingDoesNotExist() throws Exception {
        AddAmenityToGuestRequest request = new AddAmenityToGuestRequest(
                1L,
                10L,
                2,
                LocalDate.of(2026, 3, 26)
        );

        when(amenityService.addAmenityToGuest(
                1L,
                10L,
                LocalDate.of(2026, 3, 26),
                2
        )).thenThrow(new BookingNotFoundException(1L));

        mockMvc.perform(post("/api/guests/1/amenities")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(amenityService).addAmenityToGuest(
                1L,
                10L,
                LocalDate.of(2026, 3, 26),
                2
        );
    }

    @Test
    void getGuestByIdShouldReturnNotFoundWhenGuestDoesNotExist() throws Exception {
        when(guestService.findGuestById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/guests/999"))
                .andExpect(status().isNotFound());

        verify(guestService).findGuestById(999L);
    }

    @Test
    void getGuestByPhoneShouldReturnNotFoundWhenGuestDoesNotExist() throws Exception {
        when(guestService.findGuestByPhone("0000000000")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/guests/by-phone")
                        .param("phone", "0000000000"))
                .andExpect(status().isNotFound());

        verify(guestService).findGuestByPhone("0000000000");
    }

    @Test
    void getAllGuestsShouldReturnInternalServerErrorWhenServiceThrowsException() throws Exception {
        when(guestService.viewGuestsSortedBy(any()))
                .thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/guests"))
                .andExpect(status().isInternalServerError());

        verify(guestService).viewGuestsSortedBy(any());
    }
}