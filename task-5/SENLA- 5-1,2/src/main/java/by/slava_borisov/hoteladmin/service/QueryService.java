package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.hoteladmin.model.HotelSystem;
import by.slava_borisov.hoteladmin.model.*;
import by.slava_borisov.hoteladmin.util.Messages;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class QueryService {
    private HotelSystem hotelSystem;

    public QueryService(HotelSystem hotelSystem) {
        this.hotelSystem = hotelSystem;
    }

    private List<Room> sortRooms(Comparator<Room> comparator) {
        return hotelSystem.getRooms()
                .stream()
                .sorted(comparator)
                .toList();
    }

    private List<Room> sortAvailableRooms(Comparator<Room> comparator) {
        return sortRooms(comparator)
                .stream()
                .filter(r -> r.getStatus() == RoomStatus.AVAILABLE)
                .toList();
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

    public List<Room> getAvailableRoomsSortedByPrice() {
        return sortAvailableRooms(Comparator.comparingDouble(Room::getPricePerNight));
    }

    public List<Room> getAvailableRoomsSortedByCapacity() {
        return sortAvailableRooms(Comparator.comparingInt(Room::getCapacity));
    }

    public List<Room> getAvailableRoomsSortedByStars() {
        return sortAvailableRooms(Comparator.comparingInt(Room::getStars));
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

    public List<Room> getRoomsAvailableByDate(LocalDate date) {
        return hotelSystem.getRooms()
                .stream()
                .filter(r -> r.getBookingHistory()
                        .stream()
                        .noneMatch(booking -> booking.isActive(date)))
                .toList();
    }

    public List<Booking> getLastThreeBookings(int roomId) {
        return hotelSystem.findRoomById(roomId)
                .map(room -> room.getBookingHistory()
                        .stream()
                        .sorted(Comparator.comparing(Booking::getCheckInDate).reversed())
                        .limit(3)
                        .toList())
                .orElse(List.of());
    }


    public List<AmenityUsage> getGuestAmenitiesSortedByPrice(int guestId) {
        return hotelSystem.findGuestById(guestId)
                .map(guest -> guest.getAllAmenityUsages()
                        .stream()
                        .sorted(Comparator.comparingDouble(AmenityUsage::getTotalPrice))
                        .toList())
                .orElse(List.of());
    }

    public List<AmenityUsage> getGuestAmenitiesSortedByDate(int guestId) {
        return hotelSystem.findGuestById(guestId)
                .map(guest -> guest.getAllAmenityUsages()
                        .stream()
                        .sorted(Comparator.comparing(AmenityUsage::getUsageDate))
                        .toList())
                .orElse(List.of());
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

    public Map<String, List<Amenity>> getAmenitiesGroupedByCategory() {
        return hotelSystem.getAmenities()
                .stream()
                .collect(Collectors.groupingBy(Amenity::getCategory));
    }


    public String getRoomDetails(int roomId) {
        return hotelSystem.findRoomById(roomId)
                .map(room -> {
                    String lastCheckOut = room.getBookingHistory() != null && !room.getBookingHistory().isEmpty()
                            ? String.valueOf(room.getBookingHistory().get(room.getBookingHistory().size() - 1).getCheckOutDate())
                            : "Нет";

                    return String.format(
                            Messages.ROOM_DETAILS,
                            room.getId(),
                            room.getNumber(),
                            room.getPricePerNight(),
                            room.getCapacity(),
                            room.getStars(),
                            room.getStatus(),
                            room.getCurrentGuest() != null ? room.getCurrentGuest().getFullName() : "Нет",
                            room.getBookingHistory() != null ? room.getBookingHistory().size() : 0,
                            lastCheckOut
                    );
                })
                .orElse(Messages.ROOM_NOT_FOUND_DETAILS);
    }



}


