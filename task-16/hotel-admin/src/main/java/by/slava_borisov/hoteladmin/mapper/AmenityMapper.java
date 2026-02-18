package by.slava_borisov.hoteladmin.mapper;

import by.slava_borisov.hoteladmin.dto.AmenityDto;
import by.slava_borisov.hoteladmin.model.Amenity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AmenityMapper {

    AmenityDto toDto(Amenity amenity);

    Amenity toEntity(AmenityDto amenityDto);

    List<AmenityDto> toDtoList(List<Amenity> amenities);
}
