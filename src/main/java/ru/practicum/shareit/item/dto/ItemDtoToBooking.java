package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemDtoToBooking {

    private Long id;

    private String name;

    public ItemDtoToBooking(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
