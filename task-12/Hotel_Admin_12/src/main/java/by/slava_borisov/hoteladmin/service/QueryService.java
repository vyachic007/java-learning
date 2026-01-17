package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.model.Room;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface QueryService {

    List<Booking> getLastBookings(int roomId, int limit);

    List<Room> getAllRoomsSortedByPrice();

    List<Room> getAllRoomsSortedByCapacity();

    List<Room> getAllRoomsSortedByStars();

    List<Guest> getGuestsSortedByName();

    List<Guest> getGuestsSortedByCheckOutDate();

    Map<Guest, Optional<Room>> getGuestsWithRooms();

    int countAvailableRooms();

    int countCurrentGuests();

    double calculateGuestPayment(int guestId);

    List<Amenity> getAmenitiesSortedByPrice();

    List<Amenity> getAmenitiesSortedByCategory();
}
