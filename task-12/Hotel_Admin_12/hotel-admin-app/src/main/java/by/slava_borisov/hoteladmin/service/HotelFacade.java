package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.di.Inject;
import by.slava_borisov.hoteladmin.config.ConfigManager;
import by.slava_borisov.hoteladmin.dao.AmenityDao;
import by.slava_borisov.hoteladmin.dao.AmenityUsageDao;
import by.slava_borisov.hoteladmin.dao.BookingDao;
import by.slava_borisov.hoteladmin.dao.GuestDao;
import by.slava_borisov.hoteladmin.dao.RoomDao;
import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.model.AmenityUsage;
import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.model.RoomStatus;
import by.slava_borisov.hoteladmin.ui.SortCriteria.SortCriteria;
import by.slava_borisov.hoteladmin.util.Messages;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class HotelFacade {

    @Inject
    private BookingManager bookingManager;

    @Inject
    private QueryService queryManager;

    @Inject
    private ConfigManager configManager;

    @Inject
    private RoomDao roomDao;

    @Inject
    private AmenityDao amenityDao;

    @Inject
    private AmenityUsageDao amenityUsageDao;

    @Inject
    private GuestDao guestDao;

    @Inject
    private BookingDao bookingDao;

    public Result<Room> addRoom(Room room) {
        try {
            if (roomDao.findByNumber(room.getNumber()).isPresent()) {
                return Result.failure(String.format(Messages.DUPLICATE_ROOM_NUMBER, room.getNumber()));
            }
            return Result.success(roomDao.create(room));
        } catch (IllegalArgumentException e) {
            return Result.failure(e.getMessage());
        } catch (SQLException e) {
            return Result.failure(Messages.DEFAULT_ERROR_MESSAGE);
        }
    }

    public Optional<Room> findRoomById(int roomId) {
        try {
            return roomDao.findById(roomId);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public Room findRoomByNumber(String roomNumber) {
        try {
            return roomDao.findByNumber(roomNumber).orElse(null);
        } catch (SQLException e) {
            return null;
        }
    }

    public Result<Boolean> setRoomStatus(int roomId, RoomStatus status) {
        if (!configManager.isAllowRoomStatusChange()) {
            return Result.failure(Messages.ROOM_STATUS_CHANGE_DISABLED);
        }

        try {
            if (roomDao.findById(roomId).isEmpty()) {
                return Result.failure(String.format(Messages.ROOM_NOT_FOUND, roomId));
            }
            roomDao.updateStatus(roomId, status);
            return Result.success(true);
        } catch (SQLException e) {
            return Result.failure(Messages.DEFAULT_ERROR_MESSAGE);
        }
    }


    public Result<Boolean> changeRoomPrice(int roomId, double newPrice) {
        try {
            if (roomDao.findById(roomId).isEmpty()) {
                return Result.failure(String.format(Messages.ROOM_NOT_FOUND, roomId));
            }
            roomDao.updatePricePerNight(roomId, newPrice);
            return Result.success(true);
        } catch (IllegalArgumentException e) {
            return Result.failure(e.getMessage());
        } catch (SQLException e) {
            return Result.failure(Messages.DEFAULT_ERROR_MESSAGE);
        }
    }

    public List<Room> getAvailableRoomsOnDate(LocalDate date) {
        try {
            return roomDao.findAvailableOnDate(date);
        } catch (SQLException e) {
            return List.of();
        }
    }

    public List<Room> viewAllRoomsSortedBy(SortCriteria criteria) {
        if (criteria == SortCriteria.BY_ID) {
            try {
                return roomDao.findAll();
            } catch (SQLException e) {
                return List.of();
            }
        }

        return switch (criteria) {
            case BY_PRICE -> queryManager.getAllRoomsSortedByPrice();
            case BY_CAPACITY -> queryManager.getAllRoomsSortedByCapacity();
            case BY_STARS -> queryManager.getAllRoomsSortedByStars();
            default -> List.of();
        };
    }

    public Result<Booking> checkIn(Guest guest, int roomId, LocalDate checkIn, LocalDate checkOut) {
        return bookingManager.checkIn(guest, roomId, checkIn, checkOut);
    }

    public Result<Boolean> checkOut(int roomId) {
        return bookingManager.checkOut(roomId);
    }

    public Result<Amenity> addAmenity(Amenity amenity) {
        try {
            Amenity created = amenityDao.create(amenity);
            return Result.success(created);
        } catch (SQLException e) {
            return Result.failure(Messages.DEFAULT_ERROR_MESSAGE);
        }
    }

    public List<Amenity> getAllAmenities() {
        try {
            return amenityDao.findAll();
        } catch (SQLException e) {
            return List.of();
        }
    }

    public Result<Boolean> changeAmenityPrice(int amenityId, double newPrice) {
        try {
            if (amenityDao.findById(amenityId).isEmpty()) {
                return Result.failure(String.format(Messages.AMENITY_NOT_FOUND, amenityId));
            }
            amenityDao.updatePrice(amenityId, newPrice);
            return Result.success(true);
        } catch (SQLException e) {
            return Result.failure(Messages.DEFAULT_ERROR_MESSAGE);
        }
    }

    public Optional<Amenity> findAmenityById(int amenityId) {
        try {
            return amenityDao.findById(amenityId);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public Result<AmenityUsage> addAmenityToGuest(int guestId, int amenityId, LocalDate usageDate, int quantity) {
        return bookingManager.addAmenityToGuest(guestId, amenityId, usageDate, quantity);
    }

    public Optional<Guest> findGuestById(int guestId) {
        try {
            return guestDao.findById(guestId);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public List<Guest> viewGuestsSortedBy(SortCriteria criteria) {
        return switch (criteria) {
            case BY_NAME -> queryManager.getGuestsSortedByName();
            case BY_CHECK_OUT_DATE -> queryManager.getGuestsSortedByCheckOutDate();
            case BY_ID -> {
                try {
                    yield guestDao.findAll();
                } catch (SQLException e) {
                    yield List.of();
                }
            }
            default -> {
                try {
                    yield guestDao.findAll();
                } catch (SQLException e) {
                    yield List.of();
                }
            }
        };
    }

    public int getAvailableRoomsCount() {
        return queryManager.countAvailableRooms();
    }

    public int getGuestsCount() {
        return queryManager.countCurrentGuests();
    }

    public double calculateGuestPayment(int guestId) {
        return queryManager.calculateGuestPayment(guestId);
    }

    public List<Booking> viewRoomHistory(int roomId) {
        int limit = configManager.getGuestHistoryLimit();
        return queryManager.getLastBookings(roomId, limit);
    }

    public List<Room> viewAllRoomsSortedByStars() {
        return queryManager.getAllRoomsSortedByStars();
    }

    public List<Amenity> getAmenitiesSortedByPrice() {
        return queryManager.getAmenitiesSortedByPrice();
    }

    public List<Amenity> getAmenitiesSortedByCategory() {
        return queryManager.getAmenitiesSortedByCategory();
    }

    public List<AmenityUsage> viewGuestAmenities(int guestId) {
        try {
            Optional<Booking> activeBookingOpt = bookingDao.findActiveByGuestId(guestId, LocalDate.now());
            if (activeBookingOpt.isEmpty()) {
                return List.of();
            }

            int bookingId = activeBookingOpt.get().getId();
            return amenityUsageDao.findByBookingId(bookingId);
        } catch (SQLException e) {
            return List.of();
        }
    }

    public Optional<Guest> findGuestByPhone(String phone) throws SQLException {
        return guestDao.findByPhone(phone);
    }
}
