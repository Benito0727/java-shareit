package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service("UserService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository storage;


    public User addUser(User user) {
        try {
            return storage.addUser(user);
        } catch (ValidationException | ConflictException exception) {
            throw new RuntimeException(exception);
        }
    }

    public User updateUser(long id, User user) {
        try {
            return storage.updateUser(id, user.getName(), user.getEmail());
        } catch (ConflictException | NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    public User getUserById(long id) {
        try {
            return storage.getUserById(id);
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

    public Set<User> getAllUsers() {
        if (storage.getUserList() != null) {
            return storage.getUserList().stream()
                    .sorted((o1, o2) -> (int) (o1.getId() - o2.getId()))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        } else {
            return Set.of();
        }
    }
}
