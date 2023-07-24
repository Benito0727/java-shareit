package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

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
    private final UserServiceImpl service;

    @PostMapping
    public User addUser(@RequestBody @NotNull @Valid User user) {
        return service.addUser(user);
    }

    @PatchMapping("/{userId}")
    public User updateUser(@PathVariable(value = "userId") long id,
                           @RequestBody User user) {
        return service.updateUser(id, user);
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable(value = "userId") long id) {
        return service.getUserById(id);
    }

    @DeleteMapping("/{userId}")
    public void removeUser(@PathVariable(value = "userId") long id) {
        service.removeUser(id);
    }

    @GetMapping
    public Set<User> getAllUsers() {
        return service.getAllUsers();
    }
}
