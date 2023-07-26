package ru.practicum.shareit.user.service;

import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Set;

public interface UserService {

    UserDto addUser(UserDto userDto) throws ValidationException, ConflictException;

    UserDto updateUser(long id, UserDto userDto) throws ConflictException, NotFoundException;

    UserDto getUserById(long id) throws NotFoundException;

    void removeUser(long id) throws NotFoundException;

    Set<UserDto> getAllUsers();
}
