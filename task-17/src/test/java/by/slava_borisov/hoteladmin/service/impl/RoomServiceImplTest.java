package by.slava_borisov.hoteladmin.service.impl;

import by.slava_borisov.hoteladmin.config.ConfigManager;
import by.slava_borisov.hoteladmin.dao.RoomDao;
import by.slava_borisov.hoteladmin.dto.RoomDto;
import by.slava_borisov.hoteladmin.dto.RoomStatusDto;
import by.slava_borisov.hoteladmin.dto.response.PriceResponse;
import by.slava_borisov.hoteladmin.exception.DuplicateRoomNumberException;
import by.slava_borisov.hoteladmin.exception.RoomNotFoundException;
import by.slava_borisov.hoteladmin.mapper.RoomMapper;
import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.model.RoomStatus;
import by.slava_borisov.hoteladmin.service.QueryService;
import by.slava_borisov.hoteladmin.util.SortCriteria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock
    private RoomDao roomDao;

    @Mock
    private QueryService queryManager;

    @Mock
    private ConfigManager configManager;

    @Mock
    private RoomMapper roomMapper;

    @InjectMocks
    private RoomServiceImpl roomService;


    @Test
    void addRoom() {
        RoomDto request = new RoomDto(null, "101", 100.0, RoomStatusDto.AVAILABLE, 2, 4);
        Room room = new Room();
        Room created = new Room();
        RoomDto response = new RoomDto(1L, "101", 100.0, RoomStatusDto.AVAILABLE, 2, 4);

        room.setNumber("101");
        created.setId(1L);
        created.setNumber("101");

        when(roomMapper.toEntity(request)).thenReturn(room);
        when(roomDao.findByNumber("101")).thenReturn(Optional.empty());
        when(roomDao.create(room)).thenReturn(created);
        when(roomMapper.toDto(created)).thenReturn(response);

        RoomDto result = roomService.addRoom(request);

        assertEquals(1L, result.id());
        assertEquals("101", result.number());
        assertEquals(100.0, result.pricePerNight());
        assertEquals(RoomStatusDto.AVAILABLE, result.status());

        verify(roomMapper).toEntity(request);
        verify(roomDao).findByNumber("101");
        verify(roomDao).create(room);
        verify(roomMapper).toDto(created);
    }

    @Test
    void addRoomShouldThrowDuplicateRoomNumberExceptionWhenRoomAlreadyExists() {
        RoomDto request = new RoomDto(null, "101", 100.0, RoomStatusDto.AVAILABLE, 2, 4);
        Room room = new Room();
        Room existing = new Room();

        room.setNumber("101");
        existing.setNumber("101");

        when(roomMapper.toEntity(request)).thenReturn(room);
        when(roomDao.findByNumber("101")).thenReturn(Optional.of(existing));

        assertThrows(DuplicateRoomNumberException.class, () -> roomService.addRoom(request));

        verify(roomMapper).toEntity(request);
        verify(roomDao).findByNumber("101");
    }

    @Test
    void findRoomById() {
        Room room = new Room();
        RoomDto dto = new RoomDto(1L, "101", 100.0, RoomStatusDto.AVAILABLE, 2, 4);

        when(roomDao.findById(1L)).thenReturn(Optional.of(room));
        when(roomMapper.toDto(room)).thenReturn(dto);

        Optional<RoomDto> result = roomService.findRoomById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().id());
        assertEquals("101", result.get().number());

        verify(roomDao).findById(1L);
        verify(roomMapper).toDto(room);
    }

    @Test
    void findRoomByIdShouldReturnEmptyWhenRoomDoesNotExist() {
        when(roomDao.findById(999L)).thenReturn(Optional.empty());

        Optional<RoomDto> result = roomService.findRoomById(999L);

        assertTrue(result.isEmpty());

        verify(roomDao).findById(999L);
    }

    @Test
    void findRoomByNumber() {
        Room room = new Room();
        RoomDto dto = new RoomDto(1L, "101", 100.0, RoomStatusDto.AVAILABLE, 2, 4);

        when(roomDao.findByNumber("101")).thenReturn(Optional.of(room));
        when(roomMapper.toDto(room)).thenReturn(dto);

        Optional<RoomDto> result = roomService.findRoomByNumber("101");

        assertTrue(result.isPresent());
        assertEquals("101", result.get().number());

        verify(roomDao).findByNumber("101");
        verify(roomMapper).toDto(room);
    }

    @Test
    void findRoomByNumberShouldReturnEmptyWhenRoomDoesNotExist() {
        when(roomDao.findByNumber("999")).thenReturn(Optional.empty());

        Optional<RoomDto> result = roomService.findRoomByNumber("999");

        assertTrue(result.isEmpty());

        verify(roomDao).findByNumber("999");
    }

    @Test
    void setRoomStatus() {
        Room room = new Room();

        when(configManager.isAllowRoomStatusChange()).thenReturn(true);
        when(roomDao.findById(1L)).thenReturn(Optional.of(room));

        assertDoesNotThrow(() -> roomService.setRoomStatus(1L, RoomStatus.OCCUPIED));

        verify(configManager).isAllowRoomStatusChange();
        verify(roomDao).findById(1L);
        verify(roomDao).updateStatus(1L, RoomStatus.OCCUPIED);
    }

    @Test
    void setRoomStatusShouldThrowIllegalStateExceptionWhenStatusChangeDisabled() {
        when(configManager.isAllowRoomStatusChange()).thenReturn(false);

        assertThrows(IllegalStateException.class,
                () -> roomService.setRoomStatus(1L, RoomStatus.OCCUPIED));

        verify(configManager).isAllowRoomStatusChange();
    }

    @Test
    void setRoomStatusShouldThrowRoomNotFoundExceptionWhenRoomDoesNotExist() {
        when(configManager.isAllowRoomStatusChange()).thenReturn(true);
        when(roomDao.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class,
                () -> roomService.setRoomStatus(999L, RoomStatus.OCCUPIED));

        verify(configManager).isAllowRoomStatusChange();
        verify(roomDao).findById(999L);
    }

    @Test
    void changeRoomPrice() {
        Room room = new Room();

        when(roomDao.findById(1L)).thenReturn(Optional.of(room));

        assertDoesNotThrow(() -> roomService.changeRoomPrice(1L, 150.0));

        verify(roomDao).findById(1L);
        verify(roomDao).updatePricePerNight(1L, 150.0);
    }

    @Test
    void changeRoomPriceShouldThrowRoomNotFoundExceptionWhenRoomDoesNotExist() {
        when(roomDao.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class,
                () -> roomService.changeRoomPrice(999L, 150.0));

        verify(roomDao).findById(999L);
    }

    @Test
    void getAvailableRoomsOnDate() {
        Room room1 = new Room();
        Room room2 = new Room();

        RoomDto dto1 = new RoomDto(1L, "101", 100.0, RoomStatusDto.AVAILABLE, 2, 4);
        RoomDto dto2 = new RoomDto(2L, "102", 150.0, RoomStatusDto.AVAILABLE, 3, 5);

        LocalDate date = LocalDate.of(2026, 3, 26);

        when(roomDao.findAvailableOnDate(date)).thenReturn(List.of(room1, room2));
        when(roomMapper.toDto(room1)).thenReturn(dto1);
        when(roomMapper.toDto(room2)).thenReturn(dto2);

        List<RoomDto> result = roomService.getAvailableRoomsOnDate(date);

        assertEquals(2, result.size());
        assertEquals("101", result.get(0).number());
        assertEquals("102", result.get(1).number());

        verify(roomDao).findAvailableOnDate(date);
        verify(roomMapper).toDto(room1);
        verify(roomMapper).toDto(room2);
    }

    @Test
    void calculateRoomPrice() {
        Room room = new Room();
        room.setNumber("101");
        room.setPricePerNight(100.0);

        when(roomDao.findById(1L)).thenReturn(Optional.of(room));

        PriceResponse result = roomService.calculateRoomPrice(1L, "2026-03-26", "2026-03-30");

        assertEquals(400.0, result.totalPrice());
        assertEquals(100.0, result.pricePerNight());
        assertEquals(4L, result.nights());
        assertEquals("101", result.roomNumber());

        verify(roomDao).findById(1L);
    }

    @Test
    void calculateRoomPriceShouldThrowRoomNotFoundExceptionWhenRoomDoesNotExist() {
        when(roomDao.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class,
                () -> roomService.calculateRoomPrice(999L, "2026-03-26", "2026-03-30"));

        verify(roomDao).findById(999L);
    }

    @Test
    void viewAllRoomsSortedBy() {
        Room room1 = new Room();
        Room room2 = new Room();

        RoomDto dto1 = new RoomDto(1L, "101", 100.0, RoomStatusDto.AVAILABLE, 2, 4);
        RoomDto dto2 = new RoomDto(2L, "102", 150.0, RoomStatusDto.OCCUPIED, 3, 5);

        when(roomDao.findAll()).thenReturn(List.of(room1, room2));
        when(roomMapper.toDto(room1)).thenReturn(dto1);
        when(roomMapper.toDto(room2)).thenReturn(dto2);

        List<RoomDto> result = roomService.viewAllRoomsSortedBy(SortCriteria.BY_ID);

        assertEquals(2, result.size());
        assertEquals("101", result.get(0).number());
        assertEquals("102", result.get(1).number());

        verify(roomDao).findAll();
        verify(roomMapper).toDto(room1);
        verify(roomMapper).toDto(room2);
    }

    @Test
    void viewAllRoomsSortedByShouldReturnSortedByPrice() {
        Room room = new Room();
        RoomDto dto = new RoomDto(1L, "101", 100.0, RoomStatusDto.AVAILABLE, 2, 4);

        when(queryManager.getAllRoomsSortedByPrice()).thenReturn(List.of(room));
        when(roomMapper.toDto(room)).thenReturn(dto);

        List<RoomDto> result = roomService.viewAllRoomsSortedBy(SortCriteria.BY_PRICE);

        assertEquals(1, result.size());
        assertEquals("101", result.get(0).number());

        verify(queryManager).getAllRoomsSortedByPrice();
        verify(roomMapper).toDto(room);
    }

    @Test
    void viewAllRoomsSortedByShouldReturnSortedByCapacity() {
        Room room = new Room();
        RoomDto dto = new RoomDto(1L, "101", 100.0, RoomStatusDto.AVAILABLE, 2, 4);

        when(queryManager.getAllRoomsSortedByCapacity()).thenReturn(List.of(room));
        when(roomMapper.toDto(room)).thenReturn(dto);

        List<RoomDto> result = roomService.viewAllRoomsSortedBy(SortCriteria.BY_CAPACITY);

        assertEquals(1, result.size());

        verify(queryManager).getAllRoomsSortedByCapacity();
        verify(roomMapper).toDto(room);
    }

    @Test
    void viewAllRoomsSortedByShouldReturnSortedByStars() {
        Room room = new Room();
        RoomDto dto = new RoomDto(1L, "101", 100.0, RoomStatusDto.AVAILABLE, 2, 4);

        when(queryManager.getAllRoomsSortedByStars()).thenReturn(List.of(room));
        when(roomMapper.toDto(room)).thenReturn(dto);

        List<RoomDto> result = roomService.viewAllRoomsSortedBy(SortCriteria.BY_STARS);

        assertEquals(1, result.size());

        verify(queryManager).getAllRoomsSortedByStars();
        verify(roomMapper).toDto(room);
    }


    @Test
    void viewAllRoomsSortedByShouldThrowExceptionWhenCriteriaIsNull() {
        assertThrows(NullPointerException.class,
                () -> roomService.viewAllRoomsSortedBy(null));
    }
}