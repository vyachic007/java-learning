package by.slava_borisov.hoteladmin.mapper.entity;

import by.slava_borisov.hoteladmin.dto.GuestDto;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.model.value.PhoneNumber;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface GuestMapper {

    @Mapping(target = "phone", source = "phone", qualifiedByName = "phoneNumberToString")
    GuestDto toDto(Guest guest);

    @Mapping(target = "phone", source = "phone", qualifiedByName = "stringToPhoneNumber")
    Guest toEntity(GuestDto guestDto);

    List<GuestDto> toDtoList(List<Guest> guests);

    @Named("phoneNumberToString")
    default String phoneNumberToString(PhoneNumber phoneNumber) {
        return phoneNumber == null ? null : phoneNumber.getValue();
    }

    @Named("stringToPhoneNumber")
    default PhoneNumber stringToPhoneNumber(String value) {
        return value == null ? null : new PhoneNumber(value);
    }
}