package by.slava_borisov.hoteladmin.mapper.entity;

import by.slava_borisov.hoteladmin.dto.AmenityUsageDto;
import by.slava_borisov.hoteladmin.model.AmenityUsage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface AmenityUsageMapper {

    @Mapping(target = "amenityId", source = "amenity.id")
    @Mapping(target = "bookingId", source = "booking.id")
    AmenityUsageDto toDto(AmenityUsage amenityUsage);

    AmenityUsage toEntity(AmenityUsageDto amenityUsageDto);

    List<AmenityUsageDto> toDtoList(List<AmenityUsage> amenityUsages);
}