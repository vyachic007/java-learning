package by.slava_borisov.hoteladmin.mapper.entity;

import by.slava_borisov.hoteladmin.dto.RoomDto;
import by.slava_borisov.hoteladmin.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface RoomMapper {

    RoomDto toDto(Room room);

    Room toEntity(RoomDto roomDto);

    List<RoomDto> toDtoList(List<Room> rooms);
}