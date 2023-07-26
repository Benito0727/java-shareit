package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository("InMemoryUserRepository")
@Slf4j
public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();

    private long currentUserId = 0;

    @Override
    public User addUser(User user) throws ConflictException {
        if (!users.isEmpty()) {
            if (users.values().stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
                throw new ConflictException("Email занят");
            }
        }
        user.setId(++currentUserId);
        log.info("Пользователю по имени {} присвоен id: {}", user.getName(), user.getId());
        users.put(user.getId(), user);
        log.info("Пользователь с ID: {}, добавлен в хранилище", user.getId());
        return user;
    }

    @Override
    public User getUserById(long id) throws NotFoundException {
        return getUserOrThrow(id);
    }

    @Override
    public List<User> getUserList() {
        List<User> userList = new ArrayList<>(users.values());
        if (!userList.isEmpty()) {
            log.info("Собрали список пользователей");
            return userList;
        }
        return null;
    }

    @Override
    public User updateUser(long id, User user) throws ConflictException, NotFoundException {
        User userToUpdate = getUserOrThrow(id);

        if (user.getName() != null) {
            userToUpdate.setName(user.getName());
            log.info("Пользователь с ID: {} сменил имя", id);
        }

        if (user.getEmail() != null && !userToUpdate.getEmail().equals(user.getEmail())) {
            if (!users.isEmpty()) {
                if (users.values().stream()
                    .anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
                    throw new ConflictException("Email занят");
                    }
            }
            userToUpdate.setEmail(user.getEmail());
            log.info("Пользователь с ID: {} сменил email", id);
        }
        users.put(id, userToUpdate);
        return userToUpdate;
    }

    @Override
    public void deleteUserById(long id) throws NotFoundException {
        getUserOrThrow(id);
        users.remove(id);
        log.info("Удалили пользователя с ID: {}", id);
    }

    private User getUserOrThrow(long id) throws NotFoundException {
        User user = users.get(id);
        if (user != null) {
            log.info("Нашли пользователя с ID: {}", id);
            return user;
        } else {
            throw new NotFoundException(String.format("Не нашли пользователя с ID: %d", id));
        }
    }
}
