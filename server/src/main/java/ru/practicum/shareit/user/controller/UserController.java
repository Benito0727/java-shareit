package ru.practicum.shareit.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.DBUserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final DBUserService service;

    @Autowired
    public UserController(DBUserService service) {
        this.service = service;
    }

    @PostMapping
    public UserDto addUser(@RequestBody UserDto dto) {
        return service.addUser(dto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable("userId") long userId,
                                             @RequestBody UserDto dto) {
        return service.updateUser(userId, dto);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable("userId") long userId) {
        return service.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable("userId") long userId) {
        service.removeUser(userId);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return service.getAllUsers();
    }
}
