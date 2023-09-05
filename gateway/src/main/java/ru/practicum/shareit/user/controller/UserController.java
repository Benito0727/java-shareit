package ru.practicum.shareit.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@Slf4j
@Validated
public class UserController {

    private final UserClient client;

    @Autowired
    public UserController(UserClient client) {
        this.client = client;
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody @Valid UserDto dto) {
        log.info("Запрос на добавление пользователя name:{}, email={}",
                dto.getName(), dto.getEmail());
        return client.addUser(dto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable("userId") long userId,
                                             @RequestBody UserDto dto) {
        log.info("Запрос на обновление userID:{}, userDTO: {}",
                userId, dto);
        return client.updateUser(userId, dto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable("userId") long userId) {
        log.info("Запрос пользователя по userID: {}",
                userId);
        return client.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable("userId") long userId) {
        log.info("Запрос на удаление пользователя userID: {}",
                userId);
        return client.deleteUser(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Запрос списка пользователей");
        return client.getAllUsers();
    }
}
