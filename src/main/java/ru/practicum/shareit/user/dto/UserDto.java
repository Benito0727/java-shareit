package ru.practicum.shareit.user.dto;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {

    @NotNull
    private long id;

    @NotBlank
    private String name;


    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
    }
}
