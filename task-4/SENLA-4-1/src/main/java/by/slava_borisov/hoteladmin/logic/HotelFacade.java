package by.slava_borisov.hoteladmin.logic;

import by.slava_borisov.hoteladmin.model.*;
import by.slava_borisov.hoteladmin.query.HotelQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class HotelFacade {

    private HotelSystem hotelSystem;
    private BookingManager bookingManager;
    private PriceManager priceManager;
    private HotelQuery queryManager;

    public HotelFacade() {
        this.hotelSystem = new HotelSystem();
        this.bookingManager = new BookingManager(hotelSystem);
        this.priceManager = new PriceManager(hotelSystem);
        this.queryManager = new HotelQuery(hotelSystem);
    }

    public void addRoom(Room room) {
        hotelSystem.addRoom(room);
    }

    public void addAmenity(Amenity amenity) {
        hotelSystem.addAmenity(amenity);
    }

    public void checkIn(Guest guest, int roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        bookingManager.checkIn(guest, roomId, checkInDate, checkOutDate);
    }

    public void checkOut(int roomId) {
        bookingManager.checkOut(roomId);
    }

    public void addAmenityToGuest(int guestId, int amenityId, LocalDate usageDate, int quantity) {
        bookingManager.addAmenityToGuest(guestId, amenityId, usageDate, quantity);
    }

    public void updateRoomPrice(int roomId, double price) {
        priceManager.updateRoomPrice(roomId, price);
    }

    public void updateAmenityPrice(int amenityId, double price) {
        priceManager.updateAmenityPrice(amenityId, price);
    }

    public void setRoomStatus(int roomId, RoomStatus status) {
        hotelSystem.setRoomStatus(roomId, status);
    }

    public List<Room> viewAllRoomsSortedBy(String criteria) {
        return switch(criteria) {
            case "price" -> queryManager.getAllRoomsSortedByPrice();
            case "capacity" -> queryManager.getAllRoomsSortedByCapacity();
            case "stars" -> queryManager.getAllRoomsSortedByStars();
            default -> List.of();
        };
    }

    public List<Room> viewAvailableRoomsSortedBy(String criteria) {
        return switch(criteria) {
            case "price" -> queryManager.getAvailableRoomsSortedByPrice();
            case "capacity" -> queryManager.getAvailableRoomsSortedByCapacity();
            case "stars" -> queryManager.getAvailableRoomsSortedByStars();
            default -> List.of();
        };
    }

    public List<Guest> viewGuestsSortedBy(String criteria) {
        return switch(criteria) {
            case "name" -> queryManager.getGuestsSortedByName();
            case "checkoutdate" -> queryManager.getGuestsSortedByCheckOutDate();
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

    public List<AmenityUsage> viewGuestAmenities(int guestId, String sortBy) {
        return switch(sortBy) {
            case "price" -> queryManager.getGuestAmenitiesSortedByPrice(guestId);
            case "date" -> queryManager.getGuestAmenitiesSortedByDate(guestId);
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
}
