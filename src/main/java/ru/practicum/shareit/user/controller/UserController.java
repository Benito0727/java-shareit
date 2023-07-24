package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private final UserService service;

    @PostMapping
    public User addUser(@RequestBody @NotNull @Valid User user) {
        try {
            return service.addUser(user);
        } catch (ValidationException | ConflictException exception) {
            throw new RuntimeException(exception);
        }
    }

    @PatchMapping("/{userId}")
    public User updateUser(@PathVariable(value = "userId") long id,
                           @RequestBody User user) {
        try {
            return service.updateUser(id, user);
        } catch (NotFoundException | ConflictException exception) {
            throw new RuntimeException(exception);
        }
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable(value = "userId") long id) {
        try {
            return service.getUserById(id);
        } catch (NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    @DeleteMapping("/{userId}")
    public void removeUser(@PathVariable(value = "userId") long id) {
        try {
            service.removeUser(id);
        } catch (NotFoundException exception) {
            throw new RuntimeException(exception);
        }

    }

    @GetMapping
    public Set<User> getAllUsers() {
        return service.getAllUsers();
    }
}
