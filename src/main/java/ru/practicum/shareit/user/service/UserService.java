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
public class UserService {

    @Autowired
    private final UserRepository storage;


    public User addUser(User user) throws ValidationException, ConflictException {
        return storage.addUser(user);
    }

    public User updateUser(long id, User user) throws ConflictException, NotFoundException {
       return storage.updateUser(id, user.getName(), user.getEmail());
    }

    public User getUserById(long id) throws NotFoundException {
       return storage.getUserById(id);
    }

    public void removeUser(long id) throws NotFoundException {
        storage.deleteUserById(id);
    }

    public Set<User> getAllUsers() {
        return storage.getUserList().stream()
                .sorted((o1, o2) -> (int) (o1.getId() - o2.getId()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
