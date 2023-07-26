package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserEntityDtoMapper {

    public static UserDto getDtoFromEntity(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static User getEntityFromDto(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }
}
