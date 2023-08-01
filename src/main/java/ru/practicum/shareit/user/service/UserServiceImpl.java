package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final InMemoryUserRepository storage;


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

    public Set<UserDto> getAllUsers() {
        if (storage.getUserList() != null) {
            List<User> users = storage.getUserList();
            Set<UserDto> userDtoSet = new TreeSet<>((o1, o2) -> (int) (o1.getId() - o2.getId()));
            for (User user : users) {
                userDtoSet.add(UserEntityDtoMapper.getDtoFromEntity(user));
            }
            return userDtoSet.stream()
                    .sorted((o1, o2) -> (int) (o1.getId() - o2.getId()))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        } else {
            return Set.of();
        }
    }
}
