package by.slava_borisov.hoteladmin.logic;

import by.slava_borisov.hoteladmin.model.*;
import by.slava_borisov.hoteladmin.util.Messages;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class BookingManager {

    private HotelSystem hotelSystem;

    public BookingManager(HotelSystem hotelSystem) {
        this.hotelSystem = hotelSystem;
    }

    public void checkIn(Guest guest, int roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        Optional<Room> roomOpt = hotelSystem.findRoomById(roomId);

        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();

            if (room.isAvailableOnDate(checkInDate)) {
                Booking booking = new Booking(
                        hotelSystem.getAllBookings().size() + 1,
                        guest,
                        roomId,
                        checkInDate,
                        checkOutDate,
                        null,
                        new ArrayList<>()
                );
                hotelSystem.addBooking(booking);
                room.addToBookingHistory(booking);
                guest.addBooking(booking);
                room.assignGuest(guest);

                if (!hotelSystem.getGuests().contains(guest)) {
                    hotelSystem.getGuests().add(guest);
                }

                System.out.printf(Messages.CHECKIN_SUCCESS, guest.getFullName(), room.getNumber());
                return;
            }
        }
        System.out.printf(Messages.CHECKIN_ERROR, roomId);
    }

    public void checkOut(int roomId) {
        Optional<Room> roomOpt = hotelSystem.findRoomById(roomId);

        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            Booking activeBooking = hotelSystem.findActiveBookingByRoomId(roomId)
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
        Optional<Guest> guestOpt = hotelSystem.findGuestById(guestId);
        Optional<Amenity> amenityOpt = hotelSystem.findAmenityById(amenityId);

        if (guestOpt.isEmpty() || amenityOpt.isEmpty()) {
            System.out.printf(Messages.GUEST_OR_AMENITY_NOT_FOUND);
            return;
        }

        Guest guest = guestOpt.get();
        Amenity amenity = amenityOpt.get();

        Booking activeBooking = hotelSystem.findActiveBookingByGuestId(guestId)
                .stream()
                .findFirst()
                .orElse(null);

        if (activeBooking == null) {
            System.out.printf(Messages.NO_ACTIVE_BOOKING_FOR_GUEST);
            return;
        }

        AmenityUsage amenityUsage = new AmenityUsage(
                hotelSystem.getAllAmenityUsages().size() + 1,
                amenity,
                activeBooking.getId(),
                usageDate,
                quantity
        );

        activeBooking.addAmenityUsage(amenityUsage);
        hotelSystem.addAmenityUsage(amenityUsage);

        System.out.printf(Messages.AMENITY_ADDED_TO_GUEST,
                amenity.getName(), guest.getFullName(), amenityUsage.getTotalPrice());
    }
}
