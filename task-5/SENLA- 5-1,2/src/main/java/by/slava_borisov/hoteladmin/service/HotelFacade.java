package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.hoteladmin.model.*;
import by.slava_borisov.hoteladmin.ui.SortCriteria.SortCriteria;
import by.slava_borisov.hoteladmin.util.Messages;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class HotelFacade {

    private HotelSystem hotelSystem;
    private BookingManager bookingManager;
    private PriceManager priceManager;
    private QueryService queryManager;

    public HotelFacade() {
        this.hotelSystem = new HotelSystem();
        this.bookingManager = new BookingManager(hotelSystem);
        this.priceManager = new PriceManager(hotelSystem);
        this.queryManager = new QueryService(hotelSystem);
    }

    public void addRoom(Room room) {
        if (hotelSystem.getRooms().stream().anyMatch(r -> r.getNumber().equals(room.getNumber()))) {
            throw new IllegalArgumentException(Messages.ROOM_NUMBER_EXISTS);
        }
        hotelSystem.addRoom(room);
    }


    public void addAmenity(Amenity amenity) {
        hotelSystem.addAmenity(amenity);
    }

    public void addGuest(Guest guest) {
        hotelSystem.getGuests().add(guest);
    }

    public boolean deleteGuest(int guestId) {
        return hotelSystem.getGuests().removeIf(g -> g.getId() == guestId);
    }

    public List<Amenity> getAllAmenities() {
        return hotelSystem.getAmenities();
    }

    public boolean changeAmenityPrice(int amenityId, double newPrice) {
        Optional<Amenity> amenityOpt = hotelSystem.findAmenityById(amenityId);
        if (amenityOpt.isPresent()) {
            amenityOpt.get().setPrice(newPrice);
            return true;
        }
        return false;
    }

    public Result<Booking> checkIn(Guest guest, int roomId, LocalDate checkIn, LocalDate checkOut) {
        return bookingManager.checkIn(guest, roomId, checkIn, checkOut);
    }

    public Result<Boolean> checkOut(int roomId) {
        return bookingManager.checkOut(roomId);
    }

    public Result<AmenityUsage> addAmenityToGuest(int guestId, int amenityId, LocalDate usageDate, int quantity) {
        Result<AmenityUsage> result = bookingManager.addAmenityToGuest(guestId, amenityId, usageDate, quantity);
        return result;
    }

    public Optional<Guest> findGuestById(int guestId) {
        return hotelSystem.findGuestById(guestId);
    }

    public void updateRoomPrice(int roomId, double price) {
        priceManager.updateRoomPrice(roomId, price);
    }

    public Result<Boolean> updateAmenityPrice(int amenityId, double price) {
        try {
            priceManager.updateAmenityPrice(amenityId, price);
            return Result.success(true);
        } catch (IllegalArgumentException e) {
            return Result.failure(e.getMessage());
        } catch (Exception e) {
            return Result.failure(Messages.ERROR_PREFIX + "Неизвестная ошибка при обновлении цены");

        }
    }

    public Result<Boolean> setRoomStatus(int roomId, RoomStatus status) {
        Optional<Room> roomOpt = hotelSystem.findRoomById(roomId);

        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            RoomStatus oldStatus = room.getStatus();
            room.setStatus(status);
            return Result.success(true);
        }
        return Result.failure(String.format(Messages.ROOM_NOT_FOUND, roomId));
    }

    public Room findRoomByNumber(String roomNumber) {
        return hotelSystem.getRooms().stream()
                .filter(room -> room.getNumber().equals(roomNumber))
                .findFirst()
                .orElse(null);
    }

    public List<Room> viewAllRoomsSortedBy(SortCriteria criteria) {
        return switch(criteria) {
            case BY_ID -> hotelSystem.getRooms().stream()
                    .sorted((r1, r2) -> Integer.compare(r1.getId(), r2.getId()))
                    .collect(Collectors.toList());
            case BY_PRICE -> queryManager.getAllRoomsSortedByPrice();
            case BY_CAPACITY -> queryManager.getAllRoomsSortedByCapacity();
            case BY_STARS -> queryManager.getAllRoomsSortedByStars();
            default -> List.of();
        };
    }

    public List<Room> viewAvailableRoomsSortedBy(SortCriteria criteria) {
        return switch(criteria) {
            case BY_ID -> hotelSystem.getRooms().stream()
                    .filter(Room::isAvailable)
                    .sorted((r1, r2) -> Integer.compare(r1.getId(), r2.getId()))
                    .collect(Collectors.toList());
            case BY_PRICE -> queryManager.getAvailableRoomsSortedByPrice();
            case BY_CAPACITY -> queryManager.getAvailableRoomsSortedByCapacity();
            case BY_STARS -> queryManager.getAvailableRoomsSortedByStars();
            default -> List.of();
        };
    }

    public List<Guest> viewGuestsSortedBy(SortCriteria criteria) {
        return switch(criteria) {
            case BY_NAME -> queryManager.getGuestsSortedByName();
            case BY_CHECK_OUT_DATE -> queryManager.getGuestsSortedByCheckOutDate();
            default -> List.of();
        };
    }

    public int getAvailableRoomsCount() {
        return queryManager.countAvailableRooms();
    }

    public int getGuestsCount() {
        return queryManager.countCurrentGuests();
    }

    public List<Room> viewRoomsAvailableByDate(LocalDate date) {
        return queryManager.getRoomsAvailableByDate(date);
    }

    public double calculateGuestPayment(int guestId) {
        return queryManager.calculateGuestPayment(guestId);
    }

    public List<Booking> viewRoomHistory(int roomId) {
        return queryManager.getLastThreeBookings(roomId);
    }

    public List<AmenityUsage> viewGuestAmenities(int guestId, SortCriteria sortBy) {
        return switch(sortBy) {
            case BY_PRICE -> queryManager.getGuestAmenitiesSortedByPrice(guestId);
            case BY_DATE -> queryManager.getGuestAmenitiesSortedByDate(guestId);
            default -> List.of();
        };
    }

    public Map<String, List<Amenity>> viewAmenitiesByCategory() {
        return queryManager.getAmenitiesGroupedByCategory();
    }

    public PriceManager getPriceManager() {
        return priceManager;
    }

    public String getRoomDetails(int roomId) {
        return queryManager.getRoomDetails(roomId);
    }

    public Result<Boolean> changeRoomPrice(int roomId, double newPrice) {
        try {
            priceManager.updateRoomPrice(roomId, newPrice);
            return Result.success(true);
        } catch (IllegalArgumentException e) {
            return Result.failure(e.getMessage());
        } catch (Exception e) {
            return Result.failure(Messages.ROOM_NOT_FOUND);
        }
    }

    public Optional<Room> findRoomById(int roomId) {
        return hotelSystem.findRoomById(roomId);
    }

    public List<Room> getAvailableRoomsOnDate(LocalDate date) {
        return hotelSystem.getRooms().stream()
                .filter(r -> r.isAvailableOnDate(date))
                .collect(Collectors.toList());
    }
}
