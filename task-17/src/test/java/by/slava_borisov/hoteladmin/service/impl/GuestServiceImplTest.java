package by.slava_borisov.hoteladmin.service.impl;

import by.slava_borisov.hoteladmin.dao.GuestDao;
import by.slava_borisov.hoteladmin.dto.GuestDto;
import by.slava_borisov.hoteladmin.mapper.GuestMapper;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.service.QueryService;
import by.slava_borisov.hoteladmin.util.SortCriteria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GuestServiceImplTest {

    @Mock
    private GuestDao guestDao;

    @Mock
    private QueryService queryManager;

    @Mock
    private GuestMapper guestMapper;

    @InjectMocks
    private GuestServiceImpl guestService;


    @Test
    void findGuestById() {
        Guest guest = new Guest();
        GuestDto guestDto = new GuestDto(1L, "Иванов Иван Иванович", "123456789");

        when(guestDao.findById(1L)).thenReturn(Optional.of(guest));
        when(guestMapper.toDto(guest)).thenReturn(guestDto);

        Optional<GuestDto> result = guestService.findGuestById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().id());
        assertEquals("Иванов Иван Иванович", result.get().fullName());
        assertEquals("123456789", result.get().phone());

        verify(guestDao).findById(1L);
        verify(guestMapper).toDto(guest);
    }

    @Test
    void findGuestByIdShouldReturnEmptyWhenGuestDoesNotExist() {
        when(guestDao.findById(999L)).thenReturn(Optional.empty());

        Optional<GuestDto> result = guestService.findGuestById(999L);

        assertTrue(result.isEmpty());

        verify(guestDao).findById(999L);
    }

    @Test
    void findGuestByPhone() {
        Guest guest = new Guest();
        GuestDto guestDto = new GuestDto(1L, "Иванов Иван Иванович", "123456789");

        when(guestDao.findByPhone("123456789")).thenReturn(Optional.of(guest));
        when(guestMapper.toDto(guest)).thenReturn(guestDto);

        Optional<GuestDto> result = guestService.findGuestByPhone("123456789");

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().id());
        assertEquals("Иванов Иван Иванович", result.get().fullName());
        assertEquals("123456789", result.get().phone());

        verify(guestDao).findByPhone("123456789");
        verify(guestMapper).toDto(guest);
    }

    @Test
    void findGuestByPhoneShouldReturnEmptyWhenGuestDoesNotExist() {
        when(guestDao.findByPhone("000000000")).thenReturn(Optional.empty());

        Optional<GuestDto> result = guestService.findGuestByPhone("000000000");

        assertTrue(result.isEmpty());

        verify(guestDao).findByPhone("000000000");
    }

    @Test
    void viewGuestsSortedBy() {
        Guest guest1 = new Guest();
        Guest guest2 = new Guest();

        GuestDto dto1 = new GuestDto(2L, "Пушкин Александр Сергеевич", "222");
        GuestDto dto2 = new GuestDto(1L, "Иванов Иван Иванович", "111");

        when(queryManager.getGuestsSortedByName()).thenReturn(List.of(guest1, guest2));
        when(guestMapper.toDto(guest1)).thenReturn(dto1);
        when(guestMapper.toDto(guest2)).thenReturn(dto2);

        List<GuestDto> result = guestService.viewGuestsSortedBy(SortCriteria.BY_NAME);

        assertEquals(2, result.size());
        assertEquals("Иванов Иван Иванович", result.get(0).fullName());
        assertEquals("Пушкин Александр Сергеевич", result.get(1).fullName());

        verify(queryManager).getGuestsSortedByName();
        verify(guestMapper).toDto(guest1);
        verify(guestMapper).toDto(guest2);
    }

    @Test
    void viewGuestsSortedByShouldReturnSortedByCheckOutDate() {
        Guest guest1 = new Guest();
        Guest guest2 = new Guest();

        GuestDto dto1 = new GuestDto(1L, "Иванов Иван Иванович", "111");
        GuestDto dto2 = new GuestDto(2L, "Пушкин Александр Сергеевич", "222");

        when(queryManager.getGuestsSortedByCheckOutDate()).thenReturn(List.of(guest1, guest2));
        when(guestMapper.toDto(guest1)).thenReturn(dto1);
        when(guestMapper.toDto(guest2)).thenReturn(dto2);

        List<GuestDto> result = guestService.viewGuestsSortedBy(SortCriteria.BY_CHECK_OUT_DATE);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals(2L, result.get(1).id());

        verify(queryManager).getGuestsSortedByCheckOutDate();
        verify(guestMapper).toDto(guest1);
        verify(guestMapper).toDto(guest2);
    }

    @Test
    void viewGuestsSortedByShouldReturnSortedById() {
        Guest guest1 = new Guest();
        Guest guest2 = new Guest();

        GuestDto dto1 = new GuestDto(2L, "Пушкин Александр Сергеевич", "222");
        GuestDto dto2 = new GuestDto(1L, "Иванов Иван Иванович", "111");

        when(guestDao.findAll()).thenReturn(List.of(guest1, guest2));
        when(guestMapper.toDto(guest1)).thenReturn(dto1);
        when(guestMapper.toDto(guest2)).thenReturn(dto2);

        List<GuestDto> result = guestService.viewGuestsSortedBy(SortCriteria.BY_ID);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals(2L, result.get(1).id());

        verify(guestDao).findAll();
        verify(guestMapper).toDto(guest1);
        verify(guestMapper).toDto(guest2);
    }

    @Test
    void viewGuestsSortedByShouldThrowExceptionWhenCriteriaIsNull() {
        assertThrows(NullPointerException.class,
                () -> guestService.viewGuestsSortedBy(null));
    }
}