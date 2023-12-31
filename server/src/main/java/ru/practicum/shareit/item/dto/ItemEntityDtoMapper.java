package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemEntityDtoMapper {

    public static Item getItemFromItemDto(ItemDto itemDto) {
        return new Item(itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable());
    }

    public static ItemDto getItemDtoFromItem(Item item) {
        ItemDto itemDto = new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable());
        itemDto.setOwner(item.getOwner().getId());
        itemDto.setRequestId(item.getRequestId());
        return itemDto;
    }

    public static ItemDtoToRequest getItemDtoToRequest(Item item) {
        ItemDtoToRequest dto = new ItemDtoToRequest();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setRequestId(item.getRequestId());
        dto.setAvailable(item.getAvailable());
        return dto;
    }
}