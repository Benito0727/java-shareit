package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private final UserServiceImpl service;

    @PostMapping
    public UserDto addUser(@RequestBody @Valid UserDto userDto) {
        return service.addUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable(value = "userId") long id,
                           @RequestBody UserDto userDto) {
        return service.updateUser(id, userDto);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable(value = "userId") long id) {
        return service.getUserById(id);
    }

    @DeleteMapping("/{userId}")
    public void removeUser(@PathVariable(value = "userId") long id) {
        service.removeUser(id);
    }

    @GetMapping
    public Set<UserDto> getAllUsers() {
        return service.getAllUsers();
    }
}
