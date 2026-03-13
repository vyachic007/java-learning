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
import by.slava_borisov.hoteladmin.dto.response.PriceResponse;
import by.slava_borisov.hoteladmin.exception.DuplicateRoomNumberException;
import by.slava_borisov.hoteladmin.exception.RoomNotFoundException;
import by.slava_borisov.hoteladmin.exception.AmenityNotFoundException;
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
import by.slava_borisov.hoteladmin.util.SortCriteria;
import by.slava_borisov.hoteladmin.util.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class HotelFacadeService {

    private final BookingService bookingService;
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

    @Transactional
    public RoomDto addRoom(RoomDto roomDto) {
        Room room = roomMapper.toEntity(roomDto);

        if (roomDao.findByNumber(room.getNumber()).isPresent()) {
            throw new DuplicateRoomNumberException(room.getNumber());
        }

        Room created = roomDao.create(room);
        return roomMapper.toDto(created);
    }

    @Transactional(readOnly = true)
    public Optional<RoomDto> findRoomById(Long roomId) {
        return roomDao.findById(roomId)
                .map(roomMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<RoomDto> findRoomByNumber(String roomNumber) {
        return roomDao.findByNumber(roomNumber)
                .map(roomMapper::toDto);
    }

    @Transactional
    public void setRoomStatus(Long roomId, RoomStatus status) {
        if (!configManager.isAllowRoomStatusChange()) {
            throw new IllegalStateException(Messages.ROOM_STATUS_CHANGE_DISABLED);
        }

        if (roomDao.findById(roomId).isEmpty()) {
            throw new RoomNotFoundException(roomId);
        }
        roomDao.updateStatus(roomId, status);
    }


    @Transactional
    public void changeRoomPrice(Long roomId, double newPrice) {
        if (roomDao.findById(roomId).isEmpty()) {
            throw new RoomNotFoundException(roomId);
        }
        roomDao.updatePricePerNight(roomId, newPrice);
    }

    @Transactional(readOnly = true)
    public List<RoomDto> getAvailableRoomsOnDate(LocalDate date) {
        return roomDao.findAvailableOnDate(date)
                .stream()
                .map(roomMapper::toDto)
                .toList();
    }


    @Transactional
    public BookingDto checkIn(GuestDto guestDto, Long roomId, LocalDate checkIn, LocalDate checkOut) {
        Guest guest = guestMapper.toEntity(guestDto);
        Booking booking = bookingService.checkIn(guest, roomId, checkIn, checkOut);
        return bookingMapper.toDto(booking);
    }


    @Transactional
    public void checkOut(Long roomId) {
        bookingService.checkOut(roomId);
    }

    @Transactional(readOnly = true)
    public PriceResponse calculateRoomPrice(Long roomId, String checkInDateStr, String checkOutDateStr) {
        LocalDate checkIn = LocalDate.parse(checkInDateStr);
        LocalDate checkOut = LocalDate.parse(checkOutDateStr);

        Room room = roomDao.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(roomId));

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        double total = room.getPricePerNight() * nights;

        return new PriceResponse(total, room.getPricePerNight(), nights, room.getNumber());
    }



    @Transactional
    public AmenityDto addAmenity(AmenityDto amenityDto) {
        Amenity amenity = amenityMapper.toEntity(amenityDto);
        Amenity created = amenityDao.create(amenity);
        return amenityMapper.toDto(created);
    }

    @Transactional(readOnly = true)
    public List<AmenityDto> getAllAmenities() {
        return amenityDao.findAll()
                .stream()
                .map(amenityMapper::toDto)
                .toList();
    }

    @Transactional
    public void changeAmenityPrice(Long amenityId, double newPrice) {
        if (amenityDao.findById(amenityId).isEmpty()) {
            throw new AmenityNotFoundException(amenityId);
        }
        amenityDao.updatePrice(amenityId, newPrice);
    }

    @Transactional(readOnly = true)
    public Optional<AmenityDto> findAmenityById(Long amenityId) {
        return amenityDao.findById(amenityId)
                .map(amenityMapper::toDto);
    }

    @Transactional
    public AmenityUsageDto addAmenityToGuest(Long guestId, Long amenityId, LocalDate usageDate, int quantity) {
        AmenityUsage usage = bookingService.addAmenityToGuest(guestId, amenityId, usageDate, quantity);
        return amenityUsageMapper.toDto(usage);
    }

    @Transactional(readOnly = true)
    public Optional<GuestDto> findGuestById(Long guestId) {
        return guestDao.findById(guestId)
                .map(guestMapper::toDto);
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public Optional<GuestDto> findGuestByPhone(String phone) {
        return guestDao.findByPhone(phone)
                .map(guestMapper::toDto);
    }

    public List<BookingDto> viewRoomHistory(Long roomId, int limit) {
        return queryManager.getLastBookings(roomId, limit)
                .stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GuestDto> viewGuestsSortedBy(SortCriteria criteria) {
        return switch (criteria) {
            case BY_NAME -> queryManager.getGuestsSortedByName()
                    .stream().map(guestMapper::toDto)
                    .distinct()
                    .sorted(Comparator.comparing(GuestDto::fullName))
                    .toList();
            case BY_CHECK_OUT_DATE -> queryManager.getGuestsSortedByCheckOutDate()
                    .stream().map(guestMapper::toDto)
                    .distinct()
                    .toList();
            case BY_ID -> guestDao.findAll()
                    .stream().map(guestMapper::toDto)
                    .distinct()
                    .sorted(Comparator.comparing(GuestDto::id))
                    .toList();
            default -> List.of();
        };
    }

    @Transactional(readOnly = true)
    public List<AmenityDto> getAmenitiesSortedBy(SortCriteria criteria) {
        return switch (criteria) {
            case BY_PRICE -> queryManager.getAmenitiesSortedByPrice()
                    .stream().map(amenityMapper::toDto)
                    .distinct()
                    .toList();
            case BY_NAME -> queryManager.getAmenitiesSortedByCategory()
                    .stream().map(amenityMapper::toDto)
                    .distinct()
                    .toList();
            default -> amenityDao.findAll()
                    .stream().map(amenityMapper::toDto)
                    .distinct()
                    .toList();
        };
    }

    @Transactional(readOnly = true)
    public List<RoomDto> viewAllRoomsSortedBy(SortCriteria criteria) {
        return switch (criteria) {
            case BY_ID -> roomDao.findAll()
                    .stream().map(roomMapper::toDto)
                    .distinct()
                    .toList();
            case BY_PRICE -> queryManager.getAllRoomsSortedByPrice()
                    .stream().map(roomMapper::toDto)
                    .distinct()
                    .toList();
            case BY_CAPACITY -> queryManager.getAllRoomsSortedByCapacity()
                    .stream().map(roomMapper::toDto)
                    .distinct()
                    .toList();
            case BY_STARS -> queryManager.getAllRoomsSortedByStars()
                    .stream().map(roomMapper::toDto)
                    .distinct()
                    .toList();
            default -> List.of();
        };
    }
}