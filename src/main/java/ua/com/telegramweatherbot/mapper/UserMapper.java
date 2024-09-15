package ua.com.telegramweatherbot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ua.com.telegramweatherbot.model.dto.UserDto;
import ua.com.telegramweatherbot.model.entity.UserEntity;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper extends Mappable<UserEntity, UserDto> {

    @Override
    UserDto toDto(UserEntity userEntity);

    @Override
    UserEntity toEntity(UserDto userDto);
}
