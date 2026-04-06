package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.hoteladmin.dto.GuestDto;
import by.slava_borisov.hoteladmin.util.SortCriteria;

import java.util.List;

public interface GuestService {

    GuestDto getGuestById(Long guestId);

    GuestDto getGuestByPhone(String phone);

    List<GuestDto> getGuestsSortedBy(SortCriteria criteria);
}