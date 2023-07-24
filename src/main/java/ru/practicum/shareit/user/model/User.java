package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */

@Data
public class User implements Comparable<User> {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @Override
    public int compareTo(User o) {
        return (int) (this.id - o.id);
    }
}
