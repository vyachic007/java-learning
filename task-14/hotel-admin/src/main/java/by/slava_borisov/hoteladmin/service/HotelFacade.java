package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.hoteladmin.config.ConfigManager;
import by.slava_borisov.hoteladmin.dao.AmenityDao;
import by.slava_borisov.hoteladmin.dao.BookingDao;
import by.slava_borisov.hoteladmin.dao.AmenityUsageDao;
import by.slava_borisov.hoteladmin.dao.GuestDao;
import by.slava_borisov.hoteladmin.dao.RoomDao;
import by.slava_borisov.hoteladmin.dto.*;
import by.slava_borisov.hoteladmin.mapper.*;
import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.model.RoomStatus;
import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.model.AmenityUsage;
import by.slava_borisov.hoteladmin.ui.SortCriteria.SortCriteria;
import by.slava_borisov.hoteladmin.util.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class HotelFacade {

    private final BookingManager bookingManager;
    private final QueryService queryManager;
    private final ConfigManager configManager;
    private final RoomDao roomDao;
    private final AmenityDao amenityDao;
    private final AmenityUsageDao amenityUsageDao;
    private final GuestDao guestDao;
    private final BookingDao bookingDao;

    private final RoomMapper roomMapper;
    private final GuestMapper guestMapper;
    private final AmenityMapper amenityMapper;
    private final BookingMapper bookingMapper;
    private final AmenityUsageMapper amenityUsageMapper;


    public Result<RoomDto> addRoom(RoomDto roomDto) {
        try {
            Room room = roomMapper.toEntity(roomDto);
            if (roomDao.findByNumber(room.getNumber()).isPresent()) {
                return Result.failure(String.format(Messages.DUPLICATE_ROOM_NUMBER, room.getNumber()));
            }
            Room created = roomDao.create(room);
            return Result.success(roomMapper.toDto(created));
        } catch (IllegalArgumentException e) {
            return Result.failure(e.getMessage());
        } catch (SQLException e) {
            return Result.failure(Messages.DEFAULT_ERROR_MESSAGE);
        }
    }

    public Optional<RoomDto> findRoomById(Long roomId) {
        try {
            return roomDao.findById(roomId)
                    .map(roomMapper::toDto);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public RoomDto findRoomByNumber(String roomNumber) {
        try {
            return roomDao.findByNumber(roomNumber)
                    .map(roomMapper::toDto)
                    .orElse(null);
        } catch (SQLException e) {
            return null;
        }
    }

    public Result<Boolean> setRoomStatus(Long roomId, RoomStatus status) {
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


    public Result<Boolean> changeRoomPrice(Long roomId, double newPrice) {
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

    public List<RoomDto> getAvailableRoomsOnDate(LocalDate date) {
        try {
            return roomDao
                    .findAvailableOnDate(date)
                    .stream()
                    .map(roomMapper::toDto)
                    .toList();
        } catch (SQLException e) {
            return List.of();
        }
    }


    public Result<BookingDto> checkIn(GuestDto guestDto, Long roomId, LocalDate checkIn, LocalDate checkOut) {
        try {
            Guest guest = guestMapper.toEntity(guestDto);
            Result<Booking> result = bookingManager.checkIn(guest, roomId, checkIn, checkOut);

            if (result.isSuccess()) {
                return Result.success(bookingMapper.toDto(result.getData()));
            }
            return Result.failure(result.getErrorMessage());
        } catch (Exception e) {
            return Result.failure(Messages.DEFAULT_ERROR_MESSAGE);
        }
    }


    public Result<Boolean> checkOut(Long roomId) {
        return bookingManager.checkOut(roomId);
    }

    public Result<AmenityDto> addAmenity(AmenityDto amenityDto) {
        try {
            Amenity amenity = amenityMapper.toEntity(amenityDto);
            Amenity created = amenityDao.create(amenity);
            return Result.success(amenityMapper.toDto(created));
        } catch (SQLException e) {
            return Result.failure(Messages.DEFAULT_ERROR_MESSAGE);
        }
    }

    public List<GuestDto> viewGuestsSortedBy(SortCriteria criteria) {
        return switch (criteria) {
            case BY_NAME -> queryManager.getGuestsSortedByName()
                    .stream().map(guestMapper::toDto).toList();
            case BY_CHECK_OUT_DATE -> queryManager.getGuestsSortedByCheckOutDate()
                    .stream().map(guestMapper::toDto).toList();
            case BY_ID, default -> {
                try {
                    yield guestDao.findAll()
                            .stream().map(guestMapper::toDto).toList();
                } catch (SQLException e) {
                    yield List.of();
                }
            }
        };
    }


    public List<AmenityDto> getAllAmenities() {
        try {
            return amenityDao.findAll()
                    .stream()
                    .map(amenityMapper::toDto)
                    .toList();
        } catch (SQLException e) {
            return List.of();
        }
    }

    public Result<Boolean> changeAmenityPrice(Long amenityId, double newPrice) {
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

    public Optional<AmenityDto> findAmenityById(Long amenityId) {
        try {
            return amenityDao.findById(amenityId)
                    .map(amenityMapper::toDto);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public Result<AmenityUsageDto> addAmenityToGuest(Long guestId, Long amenityId, LocalDate usageDate, int quantity) {
        Result<AmenityUsage> result = bookingManager.addAmenityToGuest(guestId, amenityId, usageDate, quantity);

        if (result.isSuccess() && result.getData() != null) {
            return Result.success(amenityUsageMapper.toDto(result.getData()));
        }
        return Result.failure(result.getErrorMessage());
    }


    public Optional<GuestDto> findGuestById(Long guestId) {
        try {
            return guestDao.findById(guestId)
                    .map(guestMapper::toDto);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public List<RoomDto> viewAllRoomsSortedBy(SortCriteria criteria) {
        return switch (criteria) {
            case BY_ID -> {
                try {
                    yield roomDao.findAll()
                            .stream().map(roomMapper::toDto).toList();
                } catch (SQLException e) {
                    yield List.of();
                }
            }
            case BY_PRICE -> queryManager.getAllRoomsSortedByPrice()
                    .stream().map(roomMapper::toDto).toList();
            case BY_CAPACITY -> queryManager.getAllRoomsSortedByCapacity()
                    .stream().map(roomMapper::toDto).toList();
            case BY_STARS -> queryManager.getAllRoomsSortedByStars()
                    .stream().map(roomMapper::toDto).toList();
            default -> List.of();
        };
    }


    public int getAvailableRoomsCount() {
        return queryManager.countAvailableRooms();
    }

    public int getGuestsCount() {
        return queryManager.countCurrentGuests();
    }

    public List<BookingDto> viewRoomHistory(Long roomId) {
        int limit = configManager.getGuestHistoryLimit();
        return queryManager.getLastBookings(roomId, limit)
                .stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    public List<RoomDto> viewAllRoomsSortedByStars() {
        return queryManager.getAllRoomsSortedByStars()
                .stream()
                .map(roomMapper::toDto)
                .toList();
    }

    public List<AmenityDto> getAmenitiesSortedByPrice() {
        return queryManager.getAmenitiesSortedByPrice()
                .stream()
                .map(amenityMapper::toDto)
                .toList();
    }

    public List<AmenityDto> getAmenitiesSortedByCategory() {
        return queryManager.getAmenitiesSortedByCategory()
                .stream()
                .map(amenityMapper::toDto)
                .toList();
    }

    public List<AmenityUsageDto> viewGuestAmenities(Long guestId) {
        try {
            Optional<Booking> activeBookingOpt = bookingDao.findActiveByGuestId(guestId, LocalDate.now()); // ✅ Entity!
            if (activeBookingOpt.isEmpty()) {
                return List.of();
            }

            Long bookingId = activeBookingOpt.get().getId();
            return amenityUsageDao.findByBookingId(bookingId)
                    .stream()
                    .map(amenityUsageMapper::toDto)
                    .toList();
        } catch (SQLException e) {
            return List.of();
        }
    }


    public Optional<GuestDto> findGuestByPhone(String phone) {
        try {
            return guestDao.findByPhone(phone)
                    .map(guestMapper::toDto);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
}
