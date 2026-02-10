package by.slava_borisov.hoteladmin.mapper;

import by.slava_borisov.hoteladmin.dto.AmenityUsageDto;
import by.slava_borisov.hoteladmin.model.AmenityUsage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AmenityUsageMapper {

    AmenityUsageDto toDto(AmenityUsage amenityUsage);

    AmenityUsage toEntity(AmenityUsageDto amenityUsageDto);

    List<AmenityUsageDto> toDtoList(List<AmenityUsage> amenityUsages);
}
