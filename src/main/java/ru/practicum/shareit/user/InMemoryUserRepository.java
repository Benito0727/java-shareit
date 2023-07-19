package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.*;

@Repository
@Slf4j
public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        if (user.getId() < 0) user.setId(getCurrentId());
        log.info("Пользователю по имени {} присвоен id: {}", user.getName(), user.getId());
        users.put(user.getId(), user);
        log.info("Пользователь с ID: {}, добавлен в хранилище", user.getId());
        return user;
    }

    @Override
    public User getUserById(long id) throws NotFoundException {
        User user = getUserOrThrow(id);
        log.info("Нашли пользователя с ID: {}", id);
        return user;
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
    public User updateUser(long userId, String name, String email) throws NotFoundException {
        User user = getUserOrThrow(userId);
        if (name != null) {
            user.setName(name);
            log.info("Пользователь с ID: {} сменил имя", userId);
        }

        if (email != null) {
            user.setEmail(email);
            log.info("Пользователь с ID: {} сменил email", userId);
        }

        users.put(userId, user);
        return user;
    }

    @Override
    public void deleteUserById(long id) throws NotFoundException {
        User user = getUserOrThrow(id);
        users.remove(id);
        log.info("Удалили пользователя с ID: {}", id);
    }

    private long getCurrentId() {
        if (users.isEmpty()) {
            return 1;
        } else {
            return Collections.max(users.keySet()) + 1;
        }
    }

    private User getUserOrThrow(long id) throws NotFoundException {
        User user = users.get(id);
        if (user != null) {
            return user;
        } else {
            throw new NotFoundException(String.format("Не нашли пользователя с ид %d", id));
        }
    }
}
