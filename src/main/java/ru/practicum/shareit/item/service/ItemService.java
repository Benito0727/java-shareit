package ru.practicum.shareit.item.service;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;

import java.util.Set;

public interface ItemService {

    Item createItem(long userId, Item item) throws NotFoundException, ValidationException;

    Item updateItem(long userId, long itemId, Item item) throws NotFoundException, ValidationException;

    Item getItemById(long userId, long itemId) throws NotFoundException;

    void removeItemById(long userId, long itemId) throws NotFoundException, ValidationException;

    Set<Item> getItemSet(long userId);

    Set<Item> getItemsByText(long userId, String text) throws NotFoundException;

}
