package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.di.Inject;
import by.slava_borisov.hoteladmin.model.HotelSystem;
import by.slava_borisov.hoteladmin.model.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class QueryService {
    @Inject
    private HotelSystem hotelSystem;


    private List<Room> sortRooms(Comparator<Room> comparator) {
        return hotelSystem.getRooms()
                .stream()
                .sorted(comparator)
                .toList();
    }

    public List<Booking> getLastBookings(int roomId, int limit) {
        return hotelSystem.findRoomById(roomId)
                .map(room -> room.getBookingHistory()
                        .stream()
                        .sorted(Comparator.comparing(Booking::getCheckInDate).reversed())
                        .limit(limit)
                        .toList())
                .orElse(List.of());
    }


    public List<Room> getAllRoomsSortedByPrice() {
        return sortRooms(Comparator.comparingDouble(Room::getPricePerNight));
    }

    public List<Room> getAllRoomsSortedByCapacity() {
        return sortRooms(Comparator.comparingInt(Room::getCapacity));
    }

    public List<Room> getAllRoomsSortedByStars() {
        return sortRooms(Comparator.comparingInt(Room::getStars));
    }


    public List<Guest> getGuestsSortedByName() {
        return hotelSystem.getGuests()
                .stream()
                .sorted(Comparator.comparing(Guest::getFullName))
                .toList();
    }

    public List<Guest> getGuestsSortedByCheckOutDate() {
        return hotelSystem.getGuests()
                .stream()
                .sorted(Comparator.comparing(guest ->
                        guest.getCurrentBooking()
                                .map(Booking::getCheckOutDate)
                                .orElse(LocalDate.MAX)))
                .toList();
    }

    public Map<Guest, Optional<Room>> getGuestsWithRooms() {
        return hotelSystem.getGuests()
                .stream()
                .filter(guest -> guest.getCurrentBooking().isPresent())
                .collect(Collectors.toMap(
                        guest -> guest,
                        guest -> {
                            int roomId = guest.getCurrentBooking().get().getRoomId();
                            return hotelSystem.getRooms()
                                    .stream()
                                    .filter(room -> room.getId() == roomId)
                                    .findFirst();
                        }
                ));
    }


    public int countAvailableRooms() {
        return (int) hotelSystem.getRooms()
                .stream()
                .filter(r -> r.getStatus() == RoomStatus.AVAILABLE)
                .count();
    }


    public int countCurrentGuests() {
        return (int) hotelSystem.getGuests()
                .stream()
                .filter(g -> g.getCurrentBooking().isPresent())
                .count();
    }


    public double calculateGuestPayment(int guestId) {
        return hotelSystem.findGuestById(guestId)
                .map(guest -> {
                    double amenitiesCost = guest.getAllAmenityUsages()
                            .stream()
                            .mapToDouble(AmenityUsage::getTotalPrice)
                            .sum();

                    double roomCost = guest.getCurrentBooking()
                            .map(booking -> {
                                Room room = hotelSystem.findRoomById(booking.getRoomId()).orElse(null);
                                if (room != null) {
                                    long nights = booking.getStayDuration();
                                    return room.getPricePerNight() * nights;
                                }
                                return 0.0;
                            })
                            .orElse(0.0);

                    return amenitiesCost + roomCost;
                })
                .orElse(0.0);
    }


    public List<Amenity> getAmenitiesSortedByPrice() {
        return hotelSystem.getAmenities()
                .stream()
                .sorted(Comparator.comparingDouble(Amenity::getPrice))
                .toList();
    }

    public List<Amenity> getAmenitiesSortedByCategory() {
        return hotelSystem.getAmenities()
                .stream()
                .sorted(Comparator.comparing(Amenity::getCategory))
                .toList();
    }


}


