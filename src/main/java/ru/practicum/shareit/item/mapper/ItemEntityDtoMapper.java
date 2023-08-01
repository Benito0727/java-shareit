package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemAvailable;

public class ItemEntityDtoMapper {

    public static Item getItemFromItemDto(ItemDto itemDto) {
        return new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(), new ItemAvailable(itemDto.getId(), itemDto.getAvailable()));
    }

    public static ItemDto getItemDtoFromItem(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable().getStatus());
    }
}
