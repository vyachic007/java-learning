package by.slava_borisov.hoteladmin.logic;

import by.slava_borisov.hoteladmin.model.*;
import by.slava_borisov.hoteladmin.util.Messages;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HotelSystem {
    private List<Room> rooms = new ArrayList<>();
    private List<Guest> guests = new ArrayList<>();
    private List<Amenity> amenities = new ArrayList<>();
    private List<Booking> allBookings = new ArrayList<>();
    private List<AmenityUsage> allAmenityUsages = new ArrayList<>();

    public void addRoom(Room room) {
        rooms.add(room);
        System.out.printf((Messages.ROOM_ADDED), room.getNumber());
    }

    public void addAmenity(Amenity amenity) {
        amenities.add(amenity);
        System.out.printf((Messages.SERVICE_ADDED), amenity.getName());
    }

    public Optional<Room> findRoomById(int roomId) {
        return rooms.stream()
                .filter(r -> r.getId() == roomId)
                .findFirst();
    }


    public Optional<Amenity> findAmenityById(int serviceId) {
        return amenities.stream()
                .filter(s -> s.getId() == serviceId)
                .findFirst();
    }

    public Optional<Guest> findGuestById(int guestId) {
        return guests.stream()
                .filter(g -> g.getId() == guestId)
                .findFirst();
    }

    public List<Booking> findActiveBooking() {
        return allBookings.stream()
                .filter(b -> b.isActive(LocalDate.now()))
                .toList();
    }

    public List<Booking> findActiveBookingByGuestId(int guestId) {
        return findActiveBooking().stream()
                .filter(b -> b.getGuest().getId() == guestId)
                .toList();
    }

    public List<Booking> findActiveBookingByRoomId(int roomId) {
        return findActiveBooking().stream()
                .filter(b -> b.getRoomId() == roomId)
                .toList();
    }

    public List<Booking> findBookingsByRoomId(int roomId) {
        return allBookings.stream()
                .filter(b -> b.getRoomId() == roomId)
                .toList();
    }

    public void addBooking(Booking booking) {
        allBookings.add(booking);
    }

    public void addAmenityUsage(AmenityUsage usage) {
        allAmenityUsages.add(usage);
    }

    public void setRoomStatus(int roomId, RoomStatus status) {
        Optional<Room> roomOpt = findRoomById(roomId);
        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            RoomStatus oldStatus = room.getStatus();
            room.setStatus(status);
            System.out.printf((Messages.ROOM_STATUS_CHANGED), room.getNumber(), oldStatus, status);
        } else {
            System.out.printf((Messages.ROOM_NOT_FOUND), roomId);
        }
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Guest> getGuests() {
        return guests;
    }

    public void setGuests(List<Guest> guests) {
        this.guests = guests;
    }

    public List<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<Amenity> amenities) {
        this.amenities = amenities;
    }

    public List<Booking> getAllBookings() {
        return allBookings;
    }

    public List<AmenityUsage> getAllAmenityUsages() {
        return allAmenityUsages;
    }
}
