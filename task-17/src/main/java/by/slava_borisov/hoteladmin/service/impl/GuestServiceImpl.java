package by.slava_borisov.hoteladmin.service.impl;

import by.slava_borisov.hoteladmin.dao.GuestDao;
import by.slava_borisov.hoteladmin.dto.GuestDto;
import by.slava_borisov.hoteladmin.exception.GuestNotFoundException;
import by.slava_borisov.hoteladmin.mapper.entity.GuestMapper;
import by.slava_borisov.hoteladmin.service.GuestService;
import by.slava_borisov.hoteladmin.service.QueryService;
import by.slava_borisov.hoteladmin.util.SortCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GuestServiceImpl implements GuestService {

    private final GuestDao guestDao;
    private final QueryService queryManager;
    private final GuestMapper guestMapper;

    @Override
    @Transactional(readOnly = true)
    public GuestDto getGuestById(Long guestId) {
        log.info("Поиск гостя по id: {}", guestId);

        GuestDto result = guestDao.findById(guestId)
                .map(guestMapper::toDto)
                .orElseThrow(() -> {
                    log.warn("Гость с id={} не найден", guestId);
                    return new GuestNotFoundException(guestId);
                });

        log.info("Гость с id={} найден: {}", guestId, result.fullName());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public GuestDto getGuestByPhone(String phone) {
        log.info("Поиск гостя по телефону: {}", phone);

        GuestDto result = guestDao.findByPhone(phone)
                .map(guestMapper::toDto)
                .orElseThrow(() -> {
                    log.warn("Гость с телефоном {} не найден", phone);
                    return new GuestNotFoundException(phone);
                });

        log.info("Гость с телефоном {} найден: {}", phone, result.fullName());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GuestDto> getGuestsSortedBy(SortCriteria criteria) {
        log.info("Получение списка гостей, сортировка: {}", criteria);

        List<GuestDto> result = switch (criteria) {
            case BY_NAME -> queryManager.getGuestsSortedByName()
                    .stream()
                    .map(guestMapper::toDto)
                    .distinct()
                    .sorted(Comparator.comparing(GuestDto::fullName))
                    .toList();
            case BY_CHECK_OUT_DATE -> queryManager.getGuestsSortedByCheckOutDate()
                    .stream()
                    .map(guestMapper::toDto)
                    .distinct()
                    .toList();
            case BY_ID -> guestDao.findAll()
                    .stream()
                    .map(guestMapper::toDto)
                    .distinct()
                    .sorted(Comparator.comparing(GuestDto::id))
                    .toList();
            default -> List.of();
        };

        log.info("Найдено {} гостей", result.size());
        return result;
    }
}