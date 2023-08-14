package ru.practicum.shareit.user.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.DBUserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private final DBUserService service;

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
    public List<UserDto> getAllUsers() {
        return service.getAllUsers();
    }
}
