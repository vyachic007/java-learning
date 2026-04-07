package by.slava_borisov.hoteladmin.mapper.entity;

import by.slava_borisov.hoteladmin.dto.BookingDto;
import by.slava_borisov.hoteladmin.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface BookingMapper {

    @Mapping(target = "guestId", source = "guest.id")
    @Mapping(target = "roomId", source = "room.id")
    BookingDto toDto(Booking booking);

    Booking toEntity(BookingDto bookingDto);

    List<BookingDto> toDtoList(List<Booking> bookings);
}