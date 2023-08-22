package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;

@Data
public class ItemDtoToRequest {

    private Long id;

    private String name;

    private String description;

    private Long requestId;

    private Boolean available;

    public ItemDtoToRequest(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.requestId = item.getRequestId();
        this.available = item.getAvailable();
    }
}
