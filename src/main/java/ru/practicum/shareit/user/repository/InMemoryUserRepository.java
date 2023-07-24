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

    private long userId = 1;

    @Override
    public User addUser(User user) throws ConflictException {
        if (!users.isEmpty()) {
            if (users.values().stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
                throw new ConflictException("Email занят");
            }
        }
        if (user.getId() == null) user.setId(userId++);
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
    public User updateUser(long id, String name, String email) throws ConflictException, NotFoundException {
        User user = getUserOrThrow(id);

        if (name != null) {
            user.setName(name);
            log.info("Пользователь с ID: {} сменил имя", id);
        }

        if (email != null && !user.getEmail().equals(email)) {
            if (!users.isEmpty()) {
                if (users.values().stream()
                    .anyMatch(u -> u.getEmail().equals(email))) {
                    throw new ConflictException("Email занят");
                    }
            }
            user.setEmail(email);
            log.info("Пользователь с ID: {} сменил email", id);
        }

        users.put(id, user);
        return user;
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
