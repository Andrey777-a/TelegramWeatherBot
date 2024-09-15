package ua.com.telegramweatherbot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ua.com.telegramweatherbot.model.dto.CityDto;
import ua.com.telegramweatherbot.model.entity.CityEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CityMapper extends Mappable<CityEntity, CityDto> {
}
