package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ItemDto {

    private long id;
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Boolean available;

    public ItemDto() {
    }

    public ItemDto(Long id,
                   String name,
                   String description,
                   Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
