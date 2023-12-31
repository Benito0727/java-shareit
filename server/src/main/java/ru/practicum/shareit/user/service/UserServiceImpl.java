package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserEntityDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.InMemoryUserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service("UserService")
public class UserServiceImpl implements UserService {

    private final InMemoryUserRepository storage;

    @Autowired
    public UserServiceImpl(InMemoryUserRepository storage) {
        this.storage = storage;
    }

    public UserDto addUser(UserDto userDto) {
        try {
            User user = UserEntityDtoMapper.getEntityFromDto(userDto);
            return UserEntityDtoMapper.getDtoFromEntity(storage.addUser(user));
        } catch (ConflictException exception) {
            throw new RuntimeException(exception);
        }
    }

    public UserDto updateUser(long id, UserDto userDto) {
        try {
            User user = UserEntityDtoMapper.getEntityFromDto(userDto);
            return UserEntityDtoMapper.getDtoFromEntity(storage.updateUser(id, user));
        } catch (ConflictException | NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    public UserDto getUserById(long id) {
        try {
            return UserEntityDtoMapper.getDtoFromEntity(storage.getUserById(id));
        } catch (NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void removeUser(long id) {
        try {
            storage.deleteUserById(id);
        } catch (NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    public List<UserDto> getAllUsers() {
        if (storage.getUserList() != null) {
            return storage.getUserList().stream()
                    .map(UserEntityDtoMapper::getDtoFromEntity)
                    .sorted((o1, o2) -> (int) (o1.getId() - o2.getId()))
                    .collect(Collectors.toList());
        } else {
            return List.of();
        }
    }
}
