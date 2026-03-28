package by.slava_borisov.hoteladmin.service.impl;

import by.slava_borisov.hoteladmin.dao.AmenityDao;
import by.slava_borisov.hoteladmin.dao.AmenityUsageDao;
import by.slava_borisov.hoteladmin.dao.BookingDao;
import by.slava_borisov.hoteladmin.dto.AmenityDto;
import by.slava_borisov.hoteladmin.dto.AmenityUsageDto;
import by.slava_borisov.hoteladmin.exception.AmenityNotFoundException;
import by.slava_borisov.hoteladmin.mapper.AmenityMapper;
import by.slava_borisov.hoteladmin.mapper.AmenityUsageMapper;
import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.model.AmenityUsage;
import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.service.BookingService;
import by.slava_borisov.hoteladmin.service.QueryService;
import by.slava_borisov.hoteladmin.util.SortCriteria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AmenityServiceImplTest {

    @Mock
    private AmenityDao amenityDao;

    @Mock
    private AmenityUsageDao amenityUsageDao;

    @Mock
    private BookingDao bookingDao;

    @Mock
    private BookingService bookingService;

    @Mock
    private QueryService queryManager;

    @Mock
    private AmenityMapper amenityMapper;

    @Mock
    private AmenityUsageMapper amenityUsageMapper;

    @InjectMocks
    private AmenityServiceImpl amenityService;

    @Test
    void addAmenity() {
        AmenityDto request = new AmenityDto(null, "Завтрак", 20.0, "Еда");
        Amenity amenity = new Amenity();
        Amenity created = new Amenity();
        AmenityDto response = new AmenityDto(1L, "Завтрак", 20.0, "Еда");

        created.setId(1L);
        created.setName("Завтрак");

        when(amenityMapper.toEntity(request)).thenReturn(amenity);
        when(amenityDao.create(amenity)).thenReturn(created);
        when(amenityMapper.toDto(created)).thenReturn(response);

        AmenityDto result = amenityService.addAmenity(request);

        assertEquals(1L, result.id());
        assertEquals("Завтрак", result.name());
        assertEquals(20.0, result.price());
        assertEquals("Еда", result.category());

        verify(amenityMapper).toEntity(request);
        verify(amenityDao).create(amenity);
        verify(amenityMapper).toDto(created);
    }

    @Test
    void getAllAmenities() {
        Amenity amenity1 = new Amenity();
        Amenity amenity2 = new Amenity();

        AmenityDto dto1 = new AmenityDto(1L, "Завтрак", 20.0, "Еда");
        AmenityDto dto2 = new AmenityDto(2L, "Спа", 50.0, "Услуга");

        when(amenityDao.findAll()).thenReturn(List.of(amenity1, amenity2));
        when(amenityMapper.toDto(amenity1)).thenReturn(dto1);
        when(amenityMapper.toDto(amenity2)).thenReturn(dto2);

        List<AmenityDto> result = amenityService.getAllAmenities();

        assertEquals(2, result.size());
        assertEquals("Завтрак", result.get(0).name());
        assertEquals("Спа", result.get(1).name());

        verify(amenityDao).findAll();
        verify(amenityMapper).toDto(amenity1);
        verify(amenityMapper).toDto(amenity2);
    }

    @Test
    void changeAmenityPrice() {
        Amenity amenity = new Amenity();

        when(amenityDao.findById(1L)).thenReturn(Optional.of(amenity));

        assertDoesNotThrow(() -> amenityService.changeAmenityPrice(1L, 99.0));

        verify(amenityDao).findById(1L);
        verify(amenityDao).updatePrice(1L, 99.0);
    }

    @Test
    void changeAmenityPriceShouldThrowExceptionWhenAmenityNotFound() {
        when(amenityDao.findById(999L)).thenReturn(Optional.empty());

        assertThrows(AmenityNotFoundException.class,
                () -> amenityService.changeAmenityPrice(999L, 99.0));

        verify(amenityDao).findById(999L);
    }

    @Test
    void findAmenityById() {
        Amenity amenity = new Amenity();
        AmenityDto dto = new AmenityDto(1L, "Завтрак", 20.0, "Еда");

        when(amenityDao.findById(1L)).thenReturn(Optional.of(amenity));
        when(amenityMapper.toDto(amenity)).thenReturn(dto);

        Optional<AmenityDto> result = amenityService.findAmenityById(1L);

        assertTrue(result.isPresent());
        assertEquals("Завтрак", result.get().name());

        verify(amenityDao).findById(1L);
        verify(amenityMapper).toDto(amenity);
    }

    @Test
    void findAmenityByIdShouldReturnEmptyWhenAmenityNotFound() {
        when(amenityDao.findById(999L)).thenReturn(Optional.empty());

        Optional<AmenityDto> result = amenityService.findAmenityById(999L);

        assertTrue(result.isEmpty());

        verify(amenityDao).findById(999L);
    }

    @Test
    void getAmenitiesSortedBy() {
        Amenity amenity = new Amenity();
        AmenityDto dto = new AmenityDto(1L, "Завтрак", 20.0, "Еда");

        when(queryManager.getAmenitiesSortedByPrice()).thenReturn(List.of(amenity));
        when(amenityMapper.toDto(amenity)).thenReturn(dto);

        List<AmenityDto> result = amenityService.getAmenitiesSortedBy(SortCriteria.BY_PRICE);

        assertEquals(1, result.size());
        assertEquals("Завтрак", result.get(0).name());

        verify(queryManager).getAmenitiesSortedByPrice();
        verify(amenityMapper).toDto(amenity);
    }

    @Test
    void getAmenitiesSortedByShouldReturnDefaultList() {
        Amenity amenity = new Amenity();
        AmenityDto dto = new AmenityDto(1L, "Спа", 50.0, "Услуга");

        when(amenityDao.findAll()).thenReturn(List.of(amenity));
        when(amenityMapper.toDto(amenity)).thenReturn(dto);

        List<AmenityDto> result = amenityService.getAmenitiesSortedBy(SortCriteria.BY_ID);

        assertEquals(1, result.size());
        assertEquals("Спа", result.get(0).name());

        verify(amenityDao).findAll();
        verify(amenityMapper).toDto(amenity);
    }

    @Test
    void addAmenityToGuest() {
        AmenityUsage usage = new AmenityUsage();
        AmenityUsageDto dto = new AmenityUsageDto(
                1L,
                10L,
                100L,
                LocalDate.of(2026, 3, 26),
                2
        );

        usage.setId(1L);

        when(bookingService.addAmenityToGuest(1L, 10L, LocalDate.of(2026, 3, 26), 2))
                .thenReturn(usage);
        when(amenityUsageMapper.toDto(usage)).thenReturn(dto);

        AmenityUsageDto result = amenityService.addAmenityToGuest(
                1L,
                10L,
                LocalDate.of(2026, 3, 26),
                2
        );

        assertEquals(1L, result.id());
        assertEquals(10L, result.amenityId());
        assertEquals(2, result.quantity());

        verify(bookingService).addAmenityToGuest(1L, 10L, LocalDate.of(2026, 3, 26), 2);
        verify(amenityUsageMapper).toDto(usage);
    }

    @Test
    void viewGuestAmenities() {
        Booking booking = new Booking();
        AmenityUsage usage1 = new AmenityUsage();
        AmenityUsage usage2 = new AmenityUsage();

        AmenityUsageDto dto1 = new AmenityUsageDto(
                1L,
                10L,
                100L,
                LocalDate.of(2026, 3, 26),
                2
        );
        AmenityUsageDto dto2 = new AmenityUsageDto(
                2L,
                11L,
                100L,
                LocalDate.of(2026, 3, 27),
                1
        );

        booking.setId(100L);

        when(bookingDao.findActiveByGuestId(1L, LocalDate.now())).thenReturn(Optional.of(booking));
        when(amenityUsageDao.findByBookingId(100L)).thenReturn(List.of(usage1, usage2));
        when(amenityUsageMapper.toDto(usage1)).thenReturn(dto1);
        when(amenityUsageMapper.toDto(usage2)).thenReturn(dto2);

        List<AmenityUsageDto> result = amenityService.viewGuestAmenities(1L);

        assertEquals(2, result.size());
        assertEquals(10L, result.get(0).amenityId());
        assertEquals(11L, result.get(1).amenityId());

        verify(bookingDao).findActiveByGuestId(1L, LocalDate.now());
        verify(amenityUsageDao).findByBookingId(100L);
        verify(amenityUsageMapper).toDto(usage1);
        verify(amenityUsageMapper).toDto(usage2);
    }

    @Test
    void viewGuestAmenitiesShouldReturnEmptyListWhenNoActiveBooking() {
        when(bookingDao.findActiveByGuestId(1L, LocalDate.now())).thenReturn(Optional.empty());

        List<AmenityUsageDto> result = amenityService.viewGuestAmenities(1L);

        assertTrue(result.isEmpty());

        verify(bookingDao).findActiveByGuestId(1L, LocalDate.now());
    }
}