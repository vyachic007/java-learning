package by.slava_borisov.hoteladmin.service.impl;

import by.slava_borisov.hoteladmin.dao.RoomDao;
import by.slava_borisov.hoteladmin.dao.GuestDao;
import by.slava_borisov.hoteladmin.dao.BookingDao;
import by.slava_borisov.hoteladmin.dao.AmenityDao;
import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.service.QueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QueryServiceImpl implements QueryService {

    private final RoomDao roomDao;
    private final GuestDao guestDao;
    private final BookingDao bookingDao;
    private final AmenityDao amenityDao;

    @Override
    public int countAvailableRooms() {
        return roomDao.countAvailable();
    }

    @Override
    public int countCurrentGuests() {
        return guestDao.countCurrentGuests();
    }

    @Override
    public List<Booking> getLastBookings(Long roomId, int limit) {
        return bookingDao.findLastByRoomId(roomId, limit);
    }

    @Override
    public List<Room> getAllRoomsSortedByPrice() {
        return roomDao.findAllSortedByPrice();
    }

    @Override
    public List<Room> getAllRoomsSortedByCapacity() {
        return roomDao.findAllSortedByCapacity();
    }

    @Override
    public List<Room> getAllRoomsSortedByStars() {
        return roomDao.findAllSortedByStars();
    }

    @Override
    public List<Guest> getGuestsSortedByName() {
        return guestDao.findAllSortedByName();
    }

    @Override
    public List<Guest> getGuestsSortedByCheckOutDate() {
        return guestDao.findCurrentGuestsSortedByCheckOut();
    }

    @Override
    public List<Amenity> getAmenitiesSortedByPrice() {
        return amenityDao.findAllSortedByPrice();
    }

    @Override
    public List<Amenity> getAmenitiesSortedByCategory() {
        return amenityDao.findAllSortedByCategory();
    }
}