package ua.com.telegramweatherbot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ua.com.telegramweatherbot.Model.dto.CityDto;
import ua.com.telegramweatherbot.Model.entity.CityEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CityMapper extends Mappable<CityEntity, CityDto> {
}
