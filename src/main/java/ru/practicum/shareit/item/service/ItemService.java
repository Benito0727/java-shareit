package ru.practicum.shareit.item.service;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Set;

public interface ItemService {

    ItemDto createItem(long userId, ItemDto itemDto) throws NotFoundException, ValidationException;

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto) throws NotFoundException, ValidationException;

    ItemDto getItemById(long userId, long itemId) throws NotFoundException;

    void removeItemById(long userId, long itemId) throws NotFoundException, ValidationException;

    Set<ItemDto> getItemSet(long userId);

    Set<ItemDto> getItemsByText(long userId, String text) throws NotFoundException;

}
