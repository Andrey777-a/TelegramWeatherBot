package ua.com.telegramweatherbot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ua.com.telegramweatherbot.Model.dto.UserDto;
import ua.com.telegramweatherbot.Model.entity.UserEntity;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper extends Mappable<UserEntity, UserDto> {

    @Override
    UserDto toDto(UserEntity userEntity);

    @Override
    UserEntity toEntity(UserDto userDto);
}
