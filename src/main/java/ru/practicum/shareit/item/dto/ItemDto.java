package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */

@Getter
@Setter
public class ItemDto extends Item {

    @NotBlank
    private final String name;

    @NotBlank
    private final String description;


    public ItemDto(Item item) {
        this.name = item.getName();
        this.description = item.getDescription();

    }
}
