package by.slava_borisov.hoteladmin.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@EqualsAndHashCode(callSuper = true)
public class HotelSystem  extends Entity{
    private List<Room> rooms = new ArrayList<>();
    private List<Guest> guests = new ArrayList<>();
    private List<Amenity> amenities = new ArrayList<>();
    private List<Booking> allBookings = new ArrayList<>();
    private List<AmenityUsage> allAmenityUsages = new ArrayList<>();

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void addAmenity(Amenity amenity) {
        amenities.add(amenity);
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

    public boolean setRoomStatus(int roomId, RoomStatus status) {
        Optional<Room> roomOpt = findRoomById(roomId);
        if (roomOpt.isPresent()) {
            roomOpt.get().setStatus(status);
            return true;
        } else {
            return false;
        }
    }


}
