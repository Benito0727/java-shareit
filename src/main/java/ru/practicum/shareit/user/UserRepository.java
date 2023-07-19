package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;

public interface UserRepository {

    public User addUser(User user);

    public User getUserById(long id) throws NotFoundException;

    public List<User> getUserList();

    public User updateUser(long userId, String name, String email) throws NotFoundException;

    public void deleteUserById(long id) throws NotFoundException;
}
