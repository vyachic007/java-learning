package by.slava_borisov.hoteladmin.controller.rest;


import by.slava_borisov.hoteladmin.dto.AmenityDto;
import by.slava_borisov.hoteladmin.dto.request.ChangePriceRequest;
import by.slava_borisov.hoteladmin.exception.AmenityNotFoundException;
import by.slava_borisov.hoteladmin.exception.GlobalExceptionHandler;
import by.slava_borisov.hoteladmin.service.AmenityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
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
class AmenityRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AmenityService amenityService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        AmenityRestController controller = new AmenityRestController(amenityService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllAmenities() throws Exception {
        List<AmenityDto> amenities = List.of(
                new AmenityDto(1L, "Завтрак", 20.0, "Еда"),
                new AmenityDto(2L, "Спа", 50.0, "Услуга")
        );

        when(amenityService.getAmenitiesSortedBy(any())).thenReturn(amenities);

        mockMvc.perform(get("/api/amenities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Завтрак"))
                .andExpect(jsonPath("$[0].price").value(20.0))
                .andExpect(jsonPath("$[0].category").value("Еда"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Спа"))
                .andExpect(jsonPath("$[1].price").value(50.0))
                .andExpect(jsonPath("$[1].category").value("Услуга"));

        verify(amenityService).getAmenitiesSortedBy(any());
    }

    @Test
    void getAmenityById() throws Exception {
        AmenityDto amenity = new AmenityDto(1L, "Завтрак", 20.0, "Еда");

        when(amenityService.findAmenityById(1L)).thenReturn(Optional.of(amenity));
        mockMvc.perform(get("/api/amenities/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Завтрак"))
                .andExpect(jsonPath("$.price").value(20.0))
                .andExpect(jsonPath("$.category").value("Еда"));

        verify(amenityService).findAmenityById(1L);
    }

    @Test
    void addAmenity() throws Exception {
        AmenityDto request = new AmenityDto(null, "Завтрак", 20.0, "Еда");
        AmenityDto created = new AmenityDto(1L, "Завтрак", 20.0, "Еда");

        when(amenityService.addAmenity(any(AmenityDto.class))).thenReturn(created);

        mockMvc.perform(post("/api/amenities")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Завтрак"))
                .andExpect(jsonPath("$.price").value(20.0))
                .andExpect(jsonPath("$.category").value("Еда"));

        verify(amenityService).addAmenity(any(AmenityDto.class));
    }

    @Test
    void changeAmenityPrice() throws Exception {
        ChangePriceRequest request = new ChangePriceRequest(99.0);

        doNothing().when(amenityService).changeAmenityPrice(1L, 99.0);

        mockMvc.perform(put("/api/amenities/1/price")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(amenityService).changeAmenityPrice(1L, 99.0);
    }

    @Test
    void getAmenityByIdShouldReturnNotFoundWhenAmenityDoesNotExist() throws Exception {
        when(amenityService.findAmenityById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/amenities/999"))
                .andExpect(status().isNotFound());

        verify(amenityService).findAmenityById(999L);
    }

    @Test
    void changeAmenityPriceShouldReturnNotFoundWhenAmenityDoesNotExist() throws Exception {
        ChangePriceRequest request = new ChangePriceRequest(99.0);

        Mockito.doThrow(new AmenityNotFoundException(999L))
                .when(amenityService).changeAmenityPrice(999L, 99.0);

        mockMvc.perform(put("/api/amenities/999/price")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(amenityService).changeAmenityPrice(999L, 99.0);
    }

    @Test
    void getAllAmenitiesShouldReturnInternalServerErrorWhenServiceThrowsException() throws Exception {
        when(amenityService.getAmenitiesSortedBy(any()))
                .thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/amenities"))
                .andExpect(status().isInternalServerError());

        verify(amenityService).getAmenitiesSortedBy(any());
    }

    @Test
    void addAmenityShouldReturnInternalServerErrorWhenServiceThrowsException() throws Exception {
        AmenityDto request = new AmenityDto(null, "Завтрак", 20.0, "Еда");

        when(amenityService.addAmenity(any(AmenityDto.class)))
                .thenThrow(new RuntimeException());

        mockMvc.perform(post("/api/amenities")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());

        verify(amenityService).addAmenity(any(AmenityDto.class));
    }
}