package by.slava_borisov.hoteladmin.service.impl;

import by.slava_borisov.hoteladmin.dao.AmenityDao;
import by.slava_borisov.hoteladmin.dao.AmenityUsageDao;
import by.slava_borisov.hoteladmin.dao.BookingDao;
import by.slava_borisov.hoteladmin.dao.GuestDao;
import by.slava_borisov.hoteladmin.dao.RoomDao;
import by.slava_borisov.hoteladmin.exception.AmenityNotFoundException;
import by.slava_borisov.hoteladmin.exception.BookingNotFoundException;
import by.slava_borisov.hoteladmin.exception.GuestNotFoundException;
import by.slava_borisov.hoteladmin.exception.InvalidDateRangeException;
import by.slava_borisov.hoteladmin.exception.RoomNotAvailableException;
import by.slava_borisov.hoteladmin.exception.RoomNotFoundException;
import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.model.AmenityUsage;
import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.model.RoomStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private RoomDao roomDao;

    @Mock
    private BookingDao bookingDao;

    @Mock
    private GuestDao guestDao;

    @Mock
    private AmenityDao amenityDao;

    @Mock
    private AmenityUsageDao amenityUsageDao;

    @InjectMocks
    private BookingServiceImpl bookingService;


    @Test
    void checkIn() {
        Guest guest = org.mockito.Mockito.mock(Guest.class);
        Guest savedGuest = org.mockito.Mockito.mock(Guest.class);
        Room room = org.mockito.Mockito.mock(Room.class);
        Booking createdBooking = org.mockito.Mockito.mock(Booking.class);

        LocalDate checkInDate = LocalDate.now().plusDays(1);
        LocalDate checkOutDate = LocalDate.now().plusDays(3);

        when(guest.getFullName()).thenReturn("Иван Иванов");
        when(guest.isNew()).thenReturn(true);

        when(savedGuest.getFullName()).thenReturn("Иван Иванов");
        when(savedGuest.getId()).thenReturn(1L);

        when(roomDao.findById(10L)).thenReturn(Optional.of(room));
        when(bookingDao.isOverlappingReservationExists(10L, checkInDate, checkOutDate)).thenReturn(false);
        when(guestDao.create(guest)).thenReturn(savedGuest);
        when(bookingDao.create(any(Booking.class))).thenReturn(createdBooking);
        when(createdBooking.getId()).thenReturn(100L);

        Booking result = bookingService.checkIn(guest, 10L, checkInDate, checkOutDate);

        assertSame(createdBooking, result);

        verify(roomDao).findById(10L);
        verify(bookingDao).isOverlappingReservationExists(10L, checkInDate, checkOutDate);
        verify(guestDao).create(guest);
        verify(bookingDao).create(any(Booking.class));
        verify(roomDao).updateStatus(10L, RoomStatus.OCCUPIED);
    }

    @Test
    void checkInShouldThrowIllegalArgumentExceptionWhenGuestIsNull() {
        LocalDate checkInDate = LocalDate.now().plusDays(1);
        LocalDate checkOutDate = LocalDate.now().plusDays(3);

        assertThrows(IllegalArgumentException.class,
                () -> bookingService.checkIn(null, 10L, checkInDate, checkOutDate));
    }

    @Test
    void checkInShouldThrowInvalidDateRangeExceptionWhenDatesAreInvalid() {
        Guest guest = org.mockito.Mockito.mock(Guest.class);

        when(guest.getFullName()).thenReturn("Иван Иванов");

        LocalDate checkInDate = LocalDate.now().plusDays(5);
        LocalDate checkOutDate = LocalDate.now().plusDays(2);

        assertThrows(InvalidDateRangeException.class,
                () -> bookingService.checkIn(guest, 10L, checkInDate, checkOutDate));
    }

    @Test
    void checkInShouldThrowRoomNotFoundExceptionWhenRoomDoesNotExist() {
        Guest guest = org.mockito.Mockito.mock(Guest.class);

        when(guest.getFullName()).thenReturn("Иван Иванов");
        when(roomDao.findById(999L)).thenReturn(Optional.empty());

        LocalDate checkInDate = LocalDate.now().plusDays(1);
        LocalDate checkOutDate = LocalDate.now().plusDays(3);

        assertThrows(RoomNotFoundException.class,
                () -> bookingService.checkIn(guest, 999L, checkInDate, checkOutDate));

        verify(roomDao).findById(999L);
    }

    @Test
    void checkInShouldThrowRoomNotAvailableExceptionWhenRoomIsBusy() {
        Guest guest = org.mockito.Mockito.mock(Guest.class);
        Room room = org.mockito.Mockito.mock(Room.class);

        when(guest.getFullName()).thenReturn("Иван Иванов");
        when(roomDao.findById(10L)).thenReturn(Optional.of(room));

        LocalDate checkInDate = LocalDate.now().plusDays(1);
        LocalDate checkOutDate = LocalDate.now().plusDays(3);

        when(bookingDao.isOverlappingReservationExists(10L, checkInDate, checkOutDate)).thenReturn(true);

        assertThrows(RoomNotAvailableException.class,
                () -> bookingService.checkIn(guest, 10L, checkInDate, checkOutDate));

        verify(roomDao).findById(10L);
        verify(bookingDao).isOverlappingReservationExists(10L, checkInDate, checkOutDate);
    }

    @Test
    void checkOut() {
        Room room = org.mockito.Mockito.mock(Room.class);
        Booking booking = org.mockito.Mockito.mock(Booking.class);

        when(roomDao.findById(10L)).thenReturn(Optional.of(room));
        when(bookingDao.findActiveByRoomId(10L, LocalDate.now())).thenReturn(Optional.of(booking));
        when(booking.getId()).thenReturn(100L);

        assertDoesNotThrow(() -> bookingService.checkOut(10L));

        verify(roomDao).findById(10L);
        verify(bookingDao).findActiveByRoomId(10L, LocalDate.now());
        verify(bookingDao).updateActualCheckOutDate(100L, LocalDate.now());
        verify(roomDao).updateStatus(10L, RoomStatus.AVAILABLE);
    }

    @Test
    void checkOutShouldThrowRoomNotFoundExceptionWhenRoomDoesNotExist() {
        when(roomDao.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class,
                () -> bookingService.checkOut(999L));

        verify(roomDao).findById(999L);
    }

    @Test
    void checkOutShouldThrowBookingNotFoundExceptionWhenActiveBookingDoesNotExist() {
        Room room = org.mockito.Mockito.mock(Room.class);

        when(roomDao.findById(10L)).thenReturn(Optional.of(room));
        when(bookingDao.findActiveByRoomId(10L, LocalDate.now())).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class,
                () -> bookingService.checkOut(10L));

        verify(roomDao).findById(10L);
        verify(bookingDao).findActiveByRoomId(10L, LocalDate.now());
    }

    @Test
    void addAmenityToGuest() {
        Guest guest = org.mockito.Mockito.mock(Guest.class);
        Amenity amenity = org.mockito.Mockito.mock(Amenity.class);
        Booking booking = org.mockito.Mockito.mock(Booking.class);
        AmenityUsage createdUsage = org.mockito.Mockito.mock(AmenityUsage.class);

        when(guestDao.findById(1L)).thenReturn(Optional.of(guest));
        when(amenityDao.findById(10L)).thenReturn(Optional.of(amenity));
        when(bookingDao.findActiveByGuestId(1L, LocalDate.now())).thenReturn(Optional.of(booking));
        when(booking.getId()).thenReturn(100L);
        when(booking.getGuest()).thenReturn(guest);
        when(guest.getId()).thenReturn(1L);
        when(amenity.getId()).thenReturn(10L);
        when(createdUsage.getId()).thenReturn(500L);
        when(amenityUsageDao.create(any(AmenityUsage.class))).thenReturn(createdUsage);

        AmenityUsage result = bookingService.addAmenityToGuest(
                1L,
                10L,
                LocalDate.of(2026, 3, 26),
                2
        );

        assertSame(createdUsage, result);

        verify(guestDao).findById(1L);
        verify(amenityDao).findById(10L);
        verify(bookingDao).findActiveByGuestId(1L, LocalDate.now());
        verify(amenityUsageDao).create(any(AmenityUsage.class));
    }


    @Test
    void addAmenityToGuestShouldThrowIllegalArgumentExceptionWhenQuantityIsInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> bookingService.addAmenityToGuest(
                        1L,
                        10L,
                        LocalDate.of(2026, 3, 26),
                        0
                ));
    }

    @Test
    void addAmenityToGuestShouldThrowGuestNotFoundExceptionWhenGuestDoesNotExist() {
        when(guestDao.findById(999L)).thenReturn(Optional.empty());

        assertThrows(GuestNotFoundException.class,
                () -> bookingService.addAmenityToGuest(
                        999L,
                        10L,
                        LocalDate.of(2026, 3, 26),
                        2
                ));
        verify(guestDao).findById(999L);
    }


    @Test
    void addAmenityToGuestShouldThrowAmenityNotFoundExceptionWhenAmenityDoesNotExist() {
        Guest guest = org.mockito.Mockito.mock(Guest.class);

        when(guestDao.findById(1L)).thenReturn(Optional.of(guest));
        when(amenityDao.findById(999L)).thenReturn(Optional.empty());

        assertThrows(AmenityNotFoundException.class,
                () -> bookingService.addAmenityToGuest(
                        1L,
                        999L,
                        LocalDate.of(2026, 3, 26),
                        2
                ));

        verify(guestDao).findById(1L);
        verify(amenityDao).findById(999L);
    }


    @Test
    void addAmenityToGuestShouldThrowBookingNotFoundExceptionWhenActiveBookingDoesNotExist() {
        Guest guest = org.mockito.Mockito.mock(Guest.class);
        Amenity amenity = org.mockito.Mockito.mock(Amenity.class);

        when(guestDao.findById(1L)).thenReturn(Optional.of(guest));
        when(amenityDao.findById(10L)).thenReturn(Optional.of(amenity));
        when(bookingDao.findActiveByGuestId(1L, LocalDate.now())).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class,
                () -> bookingService.addAmenityToGuest(
                        1L,
                        10L,
                        LocalDate.of(2026, 3, 26),
                        2
                ));

        verify(guestDao).findById(1L);
        verify(amenityDao).findById(10L);
        verify(bookingDao).findActiveByGuestId(1L, LocalDate.now());
    }
}