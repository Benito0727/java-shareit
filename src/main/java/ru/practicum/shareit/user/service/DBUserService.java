package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.DBUserRepository;
import ru.practicum.shareit.user.mapper.UserEntityDtoMapper;
import ru.practicum.shareit.user.model.User;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class DBUserService implements UserService {

    private final DBUserRepository storage;

    @Autowired
    public DBUserService(DBUserRepository storage) {
        this.storage = storage;
    }

    @Override
    @Transactional
    public UserDto addUser(UserDto userDto) {
        return UserEntityDtoMapper.getDtoFromEntity(storage.save(UserEntityDtoMapper.getEntityFromDto(userDto)));
    }

    @Override
    @Transactional
    public UserDto updateUser(long id, UserDto userDto) {
        User user = checkUser(id);
        user.setId(id);
        if (userDto.getName() != null) user.setName(userDto.getName());
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
        return UserEntityDtoMapper.getDtoFromEntity(storage.save(user));
    }

    @Override
    public UserDto getUserById(long id) {
        return UserEntityDtoMapper.getDtoFromEntity(checkUser(id));
    }

    @Override
    @Transactional
    public void removeUser(long id) {
        storage.delete(checkUser(id));
    }

    @Override
    @Transactional
    public Set<UserDto> getAllUsers() {
        List<User> userList = storage.findAll();
        Set<UserDto> userSet = new HashSet<>();
        for (User user : userList) {
            userSet.add(UserEntityDtoMapper.getDtoFromEntity(user));
        }
        return userSet.stream()
                .sorted((o1, o2) -> (int) (o1.getId() - o2.getId()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private User checkUser(Long userId) {
        try {
            return storage.findById(userId).orElseThrow(
                    () -> new NotFoundException(String.format("Не нашли пользователя с ID: %d", userId))
            );
        } catch (NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }
}
