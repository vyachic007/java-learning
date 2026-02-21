package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.model.Room;

import java.util.List;

public interface QueryService {

    List<Booking> getLastBookings(Long roomId, int limit);

    List<Room> getAllRoomsSortedByPrice();

    List<Room> getAllRoomsSortedByCapacity();

    List<Room> getAllRoomsSortedByStars();

    List<Guest> getGuestsSortedByName();

    List<Guest> getGuestsSortedByCheckOutDate();

    int countAvailableRooms();

    int countCurrentGuests();

    List<Amenity> getAmenitiesSortedByPrice();

    List<Amenity> getAmenitiesSortedByCategory();
}
