package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.hoteladmin.exception.*;
import by.slava_borisov.hoteladmin.model.*;
import by.slava_borisov.hoteladmin.util.Messages;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingManager {

    private HotelSystem hotelSystem;

    public BookingManager(HotelSystem hotelSystem) {
        this.hotelSystem = hotelSystem;
    }

    public Result<Booking> checkIn(Guest guest, int roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        try {
            if (checkInDate.isAfter(checkOutDate) || checkInDate.isBefore(LocalDate.now())) {
                throw new InvalidDateRangeException(Messages.INVALID_DATE_RANGE);
            }

            Optional<Room> roomOpt = hotelSystem.findRoomById(roomId);
            if (!roomOpt.isPresent()) {
                return Result.failure(String.format(Messages.ROOM_NOT_FOUND, roomId));
            }

            Room room = roomOpt.get();

            if (!room.isAvailable()) {
                throw new RoomNotAvailableException(roomId);
            }

            if (!room.isAvailableOnDate(checkInDate)) {
                return Result.failure(String.format(Messages.ROOM_OCCUPIED_ON_DATE, roomId, checkInDate));
            }

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

            return Result.success(booking);

        } catch (RoomNotAvailableException | InvalidDateRangeException e) {
            return Result.failure(e.getMessage());
        }
    }

    public Result<Boolean> checkOut(int roomId) {
        try {
            Optional<Room> roomOpt = hotelSystem.findRoomById(roomId);

            if (roomOpt.isEmpty()) {
                return Result.failure(String.format(Messages.ROOM_NOT_FOUND, roomId));
            }

            List<Booking> bookings = hotelSystem.findActiveBookingByRoomId(roomId);
            if (bookings.isEmpty()) {
                throw new BookingNotFoundException(roomId);
            }

            Booking activeBooking = bookings.get(0);
            activeBooking.setActualCheckOutDate(LocalDate.now());
            roomOpt.get().removeGuest();

            return Result.success(true);

        } catch (BookingNotFoundException e) {
            return Result.failure(e.getMessage());
        }
    }

    public Result<AmenityUsage> addAmenityToGuest(int guestId, int amenityId, LocalDate usageDate, int quantity) {
        try {
            Optional<Guest> guestOpt = hotelSystem.findGuestById(guestId);
            if (guestOpt.isEmpty()) {
                throw new GuestNotFoundException(guestId);
            }

            Optional<Amenity> amenityOpt = hotelSystem.findAmenityById(amenityId);
            if (amenityOpt.isEmpty()) {
                throw new AmenityNotFoundException(amenityId);
            }

            Amenity amenity = amenityOpt.get();

            List<Booking> bookings = hotelSystem.findActiveBookingByGuestId(guestId);
            if (bookings.isEmpty()) {
                throw new BookingNotFoundException(guestId);
            }

            Booking activeBooking = bookings.get(0);

            AmenityUsage amenityUsage = new AmenityUsage(
                    hotelSystem.getAllAmenityUsages().size() + 1,
                    amenity,
                    activeBooking.getId(),
                    usageDate,
                    quantity
            );

            activeBooking.addAmenityUsage(amenityUsage);
            hotelSystem.addAmenityUsage(amenityUsage);

            return Result.success(amenityUsage);

        } catch (GuestNotFoundException | AmenityNotFoundException | BookingNotFoundException e) {
            return Result.failure(e.getMessage());
        }
    }
}
