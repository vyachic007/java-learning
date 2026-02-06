package by.slava_borisov.hoteladmin.mapper;

import by.slava_borisov.hoteladmin.dto.GuestDto;
import by.slava_borisov.hoteladmin.model.Guest;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GuestMapper {

    GuestDto toDto(Guest guest);

    Guest toEntity(GuestDto guestDto);

    List<GuestDto> toDtoList(List<Guest> guests);
}
