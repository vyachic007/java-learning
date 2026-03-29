package by.slava_borisov.hoteladmin.service.impl;

import by.slava_borisov.hoteladmin.dao.AmenityDao;
import by.slava_borisov.hoteladmin.dao.BookingDao;
import by.slava_borisov.hoteladmin.dao.GuestDao;
import by.slava_borisov.hoteladmin.dao.RoomDao;
import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.model.Room;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueryServiceImplTest {

    @Mock
    private RoomDao roomDao;

    @Mock
    private GuestDao guestDao;

    @Mock
    private BookingDao bookingDao;

    @Mock
    private AmenityDao amenityDao;

    @InjectMocks
    private QueryServiceImpl queryService;


    @Test
    void countAvailableRooms() {
        when(roomDao.countAvailable()).thenReturn(5);

        int result = queryService.countAvailableRooms();

        assertEquals(5, result);
        verify(roomDao).countAvailable();
    }

    @Test
    void countCurrentGuests() {
        when(guestDao.countCurrentGuests()).thenReturn(12);

        int result = queryService.countCurrentGuests();

        assertEquals(12, result);
        verify(guestDao).countCurrentGuests();
    }

    @Test
    void getLastBookings() {
        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        List<Booking> bookings = List.of(booking1, booking2);

        when(bookingDao.findLastByRoomId(1L, 10)).thenReturn(bookings);

        List<Booking> result = queryService.getLastBookings(1L, 10);

        assertEquals(2, result.size());
        assertSame(booking1, result.get(0));
        assertSame(booking2, result.get(1));
        verify(bookingDao).findLastByRoomId(1L, 10);
    }

    @Test
    void getAllRoomsSortedByPrice() {
        Room room1 = new Room();
        Room room2 = new Room();
        List<Room> rooms = List.of(room1, room2);

        when(roomDao.findAllSortedByPrice()).thenReturn(rooms);

        List<Room> result = queryService.getAllRoomsSortedByPrice();

        assertEquals(2, result.size());
        assertSame(room1, result.get(0));
        assertSame(room2, result.get(1));
        verify(roomDao).findAllSortedByPrice();
    }

    @Test
    void getAllRoomsSortedByCapacity() {
        Room room1 = new Room();
        Room room2 = new Room();
        List<Room> rooms = List.of(room1, room2);

        when(roomDao.findAllSortedByCapacity()).thenReturn(rooms);

        List<Room> result = queryService.getAllRoomsSortedByCapacity();

        assertEquals(2, result.size());
        assertSame(room1, result.get(0));
        assertSame(room2, result.get(1));
        verify(roomDao).findAllSortedByCapacity();
    }

    @Test
    void getAllRoomsSortedByStars() {
        Room room1 = new Room();
        Room room2 = new Room();
        List<Room> rooms = List.of(room1, room2);

        when(roomDao.findAllSortedByStars()).thenReturn(rooms);

        List<Room> result = queryService.getAllRoomsSortedByStars();

        assertEquals(2, result.size());
        assertSame(room1, result.get(0));
        assertSame(room2, result.get(1));
        verify(roomDao).findAllSortedByStars();
    }

    @Test
    void getGuestsSortedByName() {
        Guest guest1 = new Guest();
        Guest guest2 = new Guest();
        List<Guest> guests = List.of(guest1, guest2);

        when(guestDao.findAllSortedByName()).thenReturn(guests);

        List<Guest> result = queryService.getGuestsSortedByName();

        assertEquals(2, result.size());
        assertSame(guest1, result.get(0));
        assertSame(guest2, result.get(1));
        verify(guestDao).findAllSortedByName();
    }

    @Test
    void getGuestsSortedByCheckOutDate() {
        Guest guest1 = new Guest();
        Guest guest2 = new Guest();
        List<Guest> guests = List.of(guest1, guest2);

        when(guestDao.findCurrentGuestsSortedByCheckOut()).thenReturn(guests);

        List<Guest> result = queryService.getGuestsSortedByCheckOutDate();

        assertEquals(2, result.size());
        assertSame(guest1, result.get(0));
        assertSame(guest2, result.get(1));
        verify(guestDao).findCurrentGuestsSortedByCheckOut();
    }

    @Test
    void getAmenitiesSortedByPrice() {
        Amenity amenity1 = new Amenity();
        Amenity amenity2 = new Amenity();
        List<Amenity> amenities = List.of(amenity1, amenity2);

        when(amenityDao.findAllSortedByPrice()).thenReturn(amenities);

        List<Amenity> result = queryService.getAmenitiesSortedByPrice();

        assertEquals(2, result.size());
        assertSame(amenity1, result.get(0));
        assertSame(amenity2, result.get(1));
        verify(amenityDao).findAllSortedByPrice();
    }

    @Test
    void getAmenitiesSortedByCategory() {
        Amenity amenity1 = new Amenity();
        Amenity amenity2 = new Amenity();
        List<Amenity> amenities = List.of(amenity1, amenity2);

        when(amenityDao.findAllSortedByCategory()).thenReturn(amenities);

        List<Amenity> result = queryService.getAmenitiesSortedByCategory();

        assertEquals(2, result.size());
        assertSame(amenity1, result.get(0));
        assertSame(amenity2, result.get(1));
        verify(amenityDao).findAllSortedByCategory();
    }

    @Test
    void countAvailableRoomsShouldThrowExceptionWhenDaoFails() {
        when(roomDao.countAvailable()).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> queryService.countAvailableRooms());

        verify(roomDao).countAvailable();
    }

    @Test
    void countCurrentGuestsShouldThrowExceptionWhenDaoFails() {
        when(guestDao.countCurrentGuests()).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> queryService.countCurrentGuests());

        verify(guestDao).countCurrentGuests();
    }

    @Test
    void getLastBookingsShouldThrowExceptionWhenDaoFails() {
        when(bookingDao.findLastByRoomId(1L, 10)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> queryService.getLastBookings(1L, 10));

        verify(bookingDao).findLastByRoomId(1L, 10);
    }

    @Test
    void getAllRoomsSortedByPriceShouldThrowExceptionWhenDaoFails() {
        when(roomDao.findAllSortedByPrice()).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> queryService.getAllRoomsSortedByPrice());

        verify(roomDao).findAllSortedByPrice();
    }

    @Test
    void getAllRoomsSortedByCapacityShouldThrowExceptionWhenDaoFails() {
        when(roomDao.findAllSortedByCapacity()).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> queryService.getAllRoomsSortedByCapacity());

        verify(roomDao).findAllSortedByCapacity();
    }

    @Test
    void getAllRoomsSortedByStarsShouldThrowExceptionWhenDaoFails() {
        when(roomDao.findAllSortedByStars()).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> queryService.getAllRoomsSortedByStars());

        verify(roomDao).findAllSortedByStars();
    }

    @Test
    void getGuestsSortedByNameShouldThrowExceptionWhenDaoFails() {
        when(guestDao.findAllSortedByName()).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> queryService.getGuestsSortedByName());

        verify(guestDao).findAllSortedByName();
    }

    @Test
    void getGuestsSortedByCheckOutDateShouldThrowExceptionWhenDaoFails() {
        when(guestDao.findCurrentGuestsSortedByCheckOut()).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> queryService.getGuestsSortedByCheckOutDate());

        verify(guestDao).findCurrentGuestsSortedByCheckOut();
    }

    @Test
    void getAmenitiesSortedByPriceShouldThrowExceptionWhenDaoFails() {
        when(amenityDao.findAllSortedByPrice()).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> queryService.getAmenitiesSortedByPrice());

        verify(amenityDao).findAllSortedByPrice();
    }

    @Test
    void getAmenitiesSortedByCategoryShouldThrowExceptionWhenDaoFails() {
        when(amenityDao.findAllSortedByCategory()).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> queryService.getAmenitiesSortedByCategory());

        verify(amenityDao).findAllSortedByCategory();
    }
}