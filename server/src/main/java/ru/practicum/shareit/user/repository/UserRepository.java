package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    User addUser(User user) throws ConflictException;

    User getUserById(long id) throws NotFoundException;

    List<User> getUserList();

    User updateUser(long userId, User user) throws NotFoundException, ConflictException;

    void deleteUserById(long id) throws NotFoundException;
}
