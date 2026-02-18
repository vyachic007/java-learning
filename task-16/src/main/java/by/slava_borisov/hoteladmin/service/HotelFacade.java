package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.hoteladmin.config.ConfigManager;
import by.slava_borisov.hoteladmin.dao.AmenityDao;
import by.slava_borisov.hoteladmin.dao.BookingDao;
import by.slava_borisov.hoteladmin.dao.AmenityUsageDao;
import by.slava_borisov.hoteladmin.dao.GuestDao;
import by.slava_borisov.hoteladmin.dao.RoomDao;
import by.slava_borisov.hoteladmin.dto.RoomDto;
import by.slava_borisov.hoteladmin.dto.GuestDto;
import by.slava_borisov.hoteladmin.dto.AmenityDto;
import by.slava_borisov.hoteladmin.dto.AmenityUsageDto;
import by.slava_borisov.hoteladmin.dto.BookingDto;
import by.slava_borisov.hoteladmin.exception.*;
import by.slava_borisov.hoteladmin.mapper.AmenityUsageMapper;
import by.slava_borisov.hoteladmin.mapper.AmenityMapper;
import by.slava_borisov.hoteladmin.mapper.BookingMapper;
import by.slava_borisov.hoteladmin.mapper.GuestMapper;
import by.slava_borisov.hoteladmin.mapper.RoomMapper;
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


    public RoomDto addRoom(RoomDto roomDto) {
        Room room = roomMapper.toEntity(roomDto);

        if (roomDao.findByNumber(room.getNumber()).isPresent()) {
            throw new DuplicateRoomNumberException(room.getNumber());
        }

        Room created = roomDao.create(room);
        return roomMapper.toDto(created);
    }

    public Optional<RoomDto> findRoomById(Long roomId) {
        return roomDao.findById(roomId)
                .map(roomMapper::toDto);
    }

    public Optional<RoomDto> findRoomByNumber(String roomNumber) {
        return roomDao.findByNumber(roomNumber)
                .map(roomMapper::toDto);
    }

    public void setRoomStatus(Long roomId, RoomStatus status) {
        if (!configManager.isAllowRoomStatusChange()) {
            throw new IllegalStateException(Messages.ROOM_STATUS_CHANGE_DISABLED);
        }

        if (roomDao.findById(roomId).isEmpty()) {
            throw new RoomNotFoundException(roomId);
        }
        roomDao.updateStatus(roomId, status);
    }


    public void changeRoomPrice(Long roomId, double newPrice) {
        if (roomDao.findById(roomId).isEmpty()) {
            throw new RoomNotFoundException(roomId);
        }
        roomDao.updatePricePerNight(roomId, newPrice);
    }

    public List<RoomDto> getAvailableRoomsOnDate(LocalDate date) {
        return roomDao.findAvailableOnDate(date)
                .stream()
                .map(roomMapper::toDto)
                .toList();
    }


    public BookingDto checkIn(GuestDto guestDto, Long roomId, LocalDate checkIn, LocalDate checkOut) {
        Guest guest = guestMapper.toEntity(guestDto);
        Booking booking = bookingManager.checkIn(guest, roomId, checkIn, checkOut);
        return bookingMapper.toDto(booking);
    }


    public void checkOut(Long roomId) {
        bookingManager.checkOut(roomId);
    }

    public AmenityDto addAmenity(AmenityDto amenityDto) {
        Amenity amenity = amenityMapper.toEntity(amenityDto);
        Amenity created = amenityDao.create(amenity);
        return amenityMapper.toDto(created);
    }

    public List<GuestDto> viewGuestsSortedBy(SortCriteria criteria) {
        return switch (criteria) {
            case BY_NAME -> queryManager.getGuestsSortedByName()
                    .stream().map(guestMapper::toDto).toList();
            case BY_CHECK_OUT_DATE -> queryManager.getGuestsSortedByCheckOutDate()
                    .stream().map(guestMapper::toDto).toList();
            case BY_ID -> guestDao.findAll()
                    .stream().map(guestMapper::toDto).toList();
            default -> List.of();
        };
    }



    public List<AmenityDto> getAllAmenities() {
        return amenityDao.findAll()
                .stream()
                .map(amenityMapper::toDto)
                .toList();
    }

    public void changeAmenityPrice(Long amenityId, double newPrice) {
        if (amenityDao.findById(amenityId).isEmpty()) {
            throw new AmenityNotFoundException(amenityId);
        }
        amenityDao.updatePrice(amenityId, newPrice);
    }

    public Optional<AmenityDto> findAmenityById(Long amenityId) {
        return amenityDao.findById(amenityId)
                .map(amenityMapper::toDto);
    }

    public AmenityUsageDto addAmenityToGuest(Long guestId, Long amenityId, LocalDate usageDate, int quantity) {
        AmenityUsage usage = bookingManager.addAmenityToGuest(guestId, amenityId, usageDate, quantity);
        return amenityUsageMapper.toDto(usage);
    }


    public Optional<GuestDto> findGuestById(Long guestId) {
        return guestDao.findById(guestId)
                .map(guestMapper::toDto);
    }

    public List<RoomDto> viewAllRoomsSortedBy(SortCriteria criteria) {
        return switch (criteria) {
            case BY_ID -> roomDao.findAll()
                    .stream().map(roomMapper::toDto).toList();
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
        Optional<Booking> activeBookingOpt = bookingDao.findActiveByGuestId(guestId, LocalDate.now());
        if (activeBookingOpt.isEmpty()) {
            return List.of();
        }

        Long bookingId = activeBookingOpt.get().getId();
        return amenityUsageDao.findByBookingId(bookingId)
                .stream()
                .map(amenityUsageMapper::toDto)
                .toList();
    }


    public Optional<GuestDto> findGuestByPhone(String phone) {
        return guestDao.findByPhone(phone)
                .map(guestMapper::toDto);
    }
}