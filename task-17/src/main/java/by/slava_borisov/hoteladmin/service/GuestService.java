package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.hoteladmin.dto.GuestDto;
import by.slava_borisov.hoteladmin.util.SortCriteria;

import java.util.List;
import java.util.Optional;

public interface GuestService {

    Optional<GuestDto> findGuestById(Long guestId);

    Optional<GuestDto> findGuestByPhone(String phone);

    List<GuestDto> viewGuestsSortedBy(SortCriteria criteria);
}