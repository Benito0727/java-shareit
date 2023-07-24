package ru.practicum.shareit.user.service;

import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.Set;

public interface UserService {

    User addUser(User user) throws ValidationException, ConflictException;

    User updateUser(long id, User user) throws ConflictException, NotFoundException;

    User getUserById(long id) throws NotFoundException;

    void removeUser(long id) throws NotFoundException;

    Set<User> getAllUsers();
}
