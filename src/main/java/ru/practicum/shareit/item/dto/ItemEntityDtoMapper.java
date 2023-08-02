package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemEntityDtoMapper {

    public static Item getItemFromItemDto(ItemDto itemDto) {
        return new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), itemDto.getOwner());
    }

    public static ItemDto getItemDtoFromItem(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), item.getOwner());
    }
}