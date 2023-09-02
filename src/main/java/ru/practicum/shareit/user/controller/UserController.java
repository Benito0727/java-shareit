package ru.practicum.shareit.user.controller;

import javax.validation.Valid;
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
}
