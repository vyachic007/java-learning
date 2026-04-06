package by.slava_borisov.hoteladmin.mapper.entity;

import by.slava_borisov.hoteladmin.dto.AmenityDto;
import by.slava_borisov.hoteladmin.model.Amenity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface AmenityMapper {

    AmenityDto toDto(Amenity amenity);

    Amenity toEntity(AmenityDto amenityDto);

    List<AmenityDto> toDtoList(List<Amenity> amenities);
}