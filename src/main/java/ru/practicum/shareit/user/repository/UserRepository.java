package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    User addUser(User user) throws ValidationException, ConflictException;

    User getUserById(long id) throws NotFoundException;

    List<User> getUserList();

    User updateUser(long userId, String name, String email) throws NotFoundException, ConflictException;

    void deleteUserById(long id) throws NotFoundException;
}
