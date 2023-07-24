package ru.practicum.shareit.user.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserDto extends User {

    @NotNull
    private Long id;

    @NotBlank
    private String name;


    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
    }
}
