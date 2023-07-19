package ru.practicum.shareit.item.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */

@Data
public class Item {

    private long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private boolean available;
}
