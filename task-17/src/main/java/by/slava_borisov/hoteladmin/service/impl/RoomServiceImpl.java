package by.slava_borisov.hoteladmin.service.impl;

import by.slava_borisov.hoteladmin.config.ConfigManager;
import by.slava_borisov.hoteladmin.dao.RoomDao;
import by.slava_borisov.hoteladmin.dto.PriceDto;
import by.slava_borisov.hoteladmin.dto.RoomDto;
import by.slava_borisov.hoteladmin.exception.DuplicateRoomNumberException;
import by.slava_borisov.hoteladmin.exception.RoomNotFoundException;
import by.slava_borisov.hoteladmin.mapper.RoomMapper;
import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.model.RoomStatus;
import by.slava_borisov.hoteladmin.service.QueryService;
import by.slava_borisov.hoteladmin.service.RoomService;
import by.slava_borisov.hoteladmin.util.Messages;
import by.slava_borisov.hoteladmin.util.SortCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomDao roomDao;
    private final QueryService queryManager;
    private final ConfigManager configManager;
    private final RoomMapper roomMapper;

    @Override
    @Transactional
    public RoomDto addRoom(RoomDto roomDto) {
        log.info("Добавление новой комнаты: номер={}, цена={}, вместимость={}, звезды={}",
                roomDto.number(), roomDto.pricePerNight(), roomDto.capacity(), roomDto.stars());

        Room room = roomMapper.toEntity(roomDto);

        if (roomDao.findByNumber(room.getNumber()).isPresent()) {
            log.error("Комната с номером {} уже существует", room.getNumber());
            throw new DuplicateRoomNumberException(room.getNumber());
        }

        Room created = roomDao.create(room);
        log.info("Комната добавлена: id={}, номер={}", created.getId(), created.getNumber());
        return roomMapper.toDto(created);
    }

    @Override
    @Transactional(readOnly = true)
    public RoomDto getRoomById(Long roomId) {
        log.info("Поиск комнаты по id: {}", roomId);

        RoomDto result = roomDao.findById(roomId)
                .map(roomMapper::toDto)
                .orElseThrow(() -> {
                    log.warn("Комната с id={} не найдена", roomId);
                    return new RoomNotFoundException(roomId);
                });

        log.info("Комната с id={} найдена: номер={}", roomId, result.number());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public RoomDto getRoomByNumber(Integer roomNumber) {
        log.info("Поиск комнаты по номеру: {}", roomNumber);

        RoomDto result = roomDao.findByNumber(roomNumber)
                .map(roomMapper::toDto)
                .orElseThrow(() -> {
                    log.warn("Комната с номером {} не найдена", roomNumber);
                    return new RoomNotFoundException(roomNumber.longValue());
                });

        log.info("Комната с номером {} найдена: id={}", roomNumber, result.id());
        return result;
    }

    @Override
    @Transactional
    public void setRoomStatus(Long roomId, RoomStatus status) {
        log.info("Изменение статуса комнаты id={} на {}", roomId, status);

        if (!configManager.isAllowRoomStatusChange()) {
            log.error("Изменение статуса отключено в конфигурации");
            throw new IllegalStateException(Messages.ROOM_STATUS_CHANGE_DISABLED);
        }

        if (roomDao.findById(roomId).isEmpty()) {
            log.error("Комната с id={} не найдена", roomId);
            throw new RoomNotFoundException(roomId);
        }

        roomDao.updateStatus(roomId, status);
        log.info("Статус комнаты id={} успешно изменен на {}", roomId, status);
    }

    @Override
    @Transactional
    public void changeRoomPrice(Long roomId, BigDecimal newPrice) {
        log.info("Изменение цены комнаты id={}, новая цена={}", roomId, newPrice);

        if (roomDao.findById(roomId).isEmpty()) {
            log.error("Комната с id={} не найдена", roomId);
            throw new RoomNotFoundException(roomId);
        }

        roomDao.updatePricePerNight(roomId, newPrice);
        log.info("Цена комнаты id={} успешно изменена на {}", roomId, newPrice);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDto> getAvailableRoomsOnDate(LocalDate date) {
        log.info("Получение списка свободных комнат на дату: {}", date);

        List<RoomDto> result = roomDao.findAvailableOnDate(date)
                .stream()
                .map(roomMapper::toDto)
                .toList();

        log.info("Найдено {} свободных комнат на дату {}", result.size(), date);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDto> getRoomsSortedBy(SortCriteria criteria) {
        log.info("Получение списка всех комнат, сортировка: {}", criteria);

        List<RoomDto> result = switch (criteria) {
            case BY_ID -> roomDao.findAll()
                    .stream()
                    .map(roomMapper::toDto)
                    .distinct()
                    .toList();
            case BY_PRICE -> queryManager.getAllRoomsSortedByPrice()
                    .stream()
                    .map(roomMapper::toDto)
                    .distinct()
                    .toList();
            case BY_CAPACITY -> queryManager.getAllRoomsSortedByCapacity()
                    .stream()
                    .map(roomMapper::toDto)
                    .distinct()
                    .toList();
            case BY_STARS -> queryManager.getAllRoomsSortedByStars()
                    .stream()
                    .map(roomMapper::toDto)
                    .distinct()
                    .toList();
            default -> throw new IllegalArgumentException("Неверный критерий сортировки: " + criteria);
        };

        log.info("Найдено {} комнат", result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public PriceDto calculateRoomPrice(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        log.info("Расчет стоимости проживания: комната id={}, даты {}-{}",
                roomId, checkInDate, checkOutDate);

        Room room = roomDao.findById(roomId)
                .orElseThrow(() -> {
                    log.error("Комната с id={} не найдена", roomId);
                    return new RoomNotFoundException(roomId);
                });

        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        BigDecimal total = room.getPricePerNight().multiply(BigDecimal.valueOf(nights));

        log.info("Стоимость рассчитана: комната {}, цена за ночь={}, ночей={}, итого={}",
                room.getNumber(), room.getPricePerNight(), nights, total);

        return new PriceDto(total, room.getPricePerNight(), nights, room.getNumber());
    }
}