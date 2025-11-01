package by.slava_borisov.hoteladmin.logic;

import by.slava_borisov.hoteladmin.model.*;
import by.slava_borisov.hoteladmin.query.HotelQuery;
import by.slava_borisov.hoteladmin.util.Messages;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HotelSystem {

    private List<Room> rooms = new ArrayList<>();
    private List<Guest> guests = new ArrayList<>();
    private List<Amenity> amenities = new ArrayList<>();
    private List<Booking> allBookings = new ArrayList<>();
    private List<AmenityUsage> allAmenityUsages = new ArrayList<>();

    private HotelQuery queryManager;


    public HotelSystem() {
        this.queryManager = new HotelQuery(this);
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

    public void checkIn(Guest guest, int roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        Optional<Room> roomOpt = findRoomById(roomId);

        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();

            if (room.isAvailableOnDate(checkInDate)) {
                Booking booking = new Booking(
                        allBookings.size() + 1,
                        guest,
                        roomId,
                        checkInDate,
                        checkOutDate,
                        null,
                        new ArrayList<>()
                );

                allBookings.add(booking);
                room.addToBookingHistory(booking);
                guest.addBooking(booking);

                room.assignGuest(guest);

                if (!guests.contains(guest)) {
                    guests.add(guest);
                }

                System.out.printf(Messages.CHECKIN_SUCCESS,
                        guest.getFullName(), room.getNumber());
                return;
            }
        }
        System.out.printf(Messages.CHECKIN_ERROR, roomId);
    }


    public void checkOut(int roomId) {
        Optional<Room> roomOpt = findRoomById(roomId);

        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();

            Booking activeBooking = findActiveBookingByRoomId(roomId)
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (activeBooking != null) {
                String guestName = activeBooking.getGuest().getFullName();

                activeBooking.setActualCheckOutDate(LocalDate.now());
                room.removeGuest();

                System.out.printf(Messages.CHECKOUT_SUCCESS, guestName, room.getNumber());
                return;
            }
        }
        System.out.printf(Messages.ROOM_NOT_OCCUPIED, roomId);
    }


    public void addAmenityToGuest(int guestId, int amenityId, LocalDate usageDate, int quantity) {
        Optional<Guest> guestOpt = findGuestById(guestId);
        Optional<Amenity> amenityOpt = findAmenityById(amenityId);

        if (guestOpt.isEmpty() || amenityOpt.isEmpty()) {
            System.out.printf(Messages.GUEST_OR_AMENITY_NOT_FOUND);
            return;
        }

        Guest guest = guestOpt.get();
        Amenity amenity = amenityOpt.get();

        Booking activeBooking = findActiveBookingByGuestId(guestId)
                .stream()
                .findFirst()
                .orElse(null);

        if (activeBooking == null) {
            System.out.printf(Messages.NO_ACTIVE_BOOKING_FOR_GUEST);
            return;
        }

        AmenityUsage amenityUsage = new AmenityUsage(
                allAmenityUsages.size() + 1,
                amenity,
                activeBooking.getId(),
                usageDate,
                quantity
        );

        activeBooking.addAmenityUsage(amenityUsage);
        allAmenityUsages.add(amenityUsage);

        System.out.printf(Messages.AMENITY_ADDED_TO_GUEST,
                amenity.getName(), guest.getFullName(), amenityUsage.getTotalPrice());
    }



    public void setRoomStatus(int roomId, RoomStatus status) {
        Optional<Room> roomOpt = findRoomById(roomId);

        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            RoomStatus oldStatus = room.getStatus();
            room.setStatus(status);
            System.out.printf((Messages.ROOM_STATUS_CHANGED),
                    room.getNumber(), oldStatus, status);
        } else {
            System.out.printf((Messages.ROOM_NOT_FOUND), roomId);
        }
    }

    public void updateRoomPrice(int roomId, double price) {
        Optional<Room> roomOpt = findRoomById(roomId);
        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            double oldPrice = room.getPricePerNight();
            room.setPricePerNight(price);
            System.out.printf((Messages.ROOM_PRICE_CHANGED),
                    room.getNumber(), oldPrice, price);
        } else {
            System.out.printf((Messages.ROOM_NOT_FOUND), roomId);
        }
    }

    public void updateAmenityPrice(int serviceId, double price) {
        Optional<Amenity> serviceOpt = findAmenityById(serviceId);

        if (serviceOpt.isPresent()) {
            Amenity service = serviceOpt.get();
            double oldPrice = service.getPrice();
            service.setPrice(price);
            System.out.printf((Messages.SERVICE_PRICE_CHANGED),
                    service.getName(), oldPrice, price);
        } else {
            System.out.printf((Messages.SERVICE_NOT_FOUND), serviceId);
        }
    }

    public Optional<Guest> findGuestById(int guestId) {
        return guests.stream()
                .filter(g -> g.getId() == guestId)
                .findFirst();
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


    // ====== НОМЕРА ======
    public List<Room> viewAllRoomsSortedBy(String criteria) {
        // criteria: "price", "capacity", "stars"
        return switch(criteria) {
            case "price" -> queryManager.getAllRoomsSortedByPrice();
            case "capacity" -> queryManager.getAllRoomsSortedByCapacity();
            case "stars" -> queryManager.getAllRoomsSortedByStars();
            default -> List.of();
        };
    }

    public List<Room> viewAvailableRoomsSortedBy(String criteria) {
        // criteria: "price", "capacity", "stars"
        return switch(criteria) {
            case "price" -> queryManager.getAvailableRoomsSortedByPrice();
            case "capacity" -> queryManager.getAvailableRoomsSortedByCapacity();
            case "stars" -> queryManager.getAvailableRoomsSortedByStars();
            default -> List.of();
        };
    }

    // ====== ГОСТИ ======
    public List<Guest> viewGuestsSortedBy(String criteria) {
        // criteria: "name", "checkoutdate"
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

    public String getRoomDetails(int roomId) {
        return queryManager.getRoomDetails(roomId);
    }

}