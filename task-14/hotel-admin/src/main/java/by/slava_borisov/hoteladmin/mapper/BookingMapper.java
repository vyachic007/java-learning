package by.slava_borisov.hoteladmin.mapper;

import by.slava_borisov.hoteladmin.dto.BookingDto;
import by.slava_borisov.hoteladmin.model.Booking;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingDto toDto(Booking booking);

    Booking toEntity(BookingDto bookingDto);

    List<BookingDto> toDtoList(List<Booking> bookings);
}
