package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    Item addItem(long userId, Item item) throws ValidationException;

    Item getItemById(long userId, long itemId) throws NotFoundException;

    void removeItem(long userId, long itemId) throws NotFoundException, ValidationException;

    Item updateItem(long userId, long itemId, Item item) throws NotFoundException, ValidationException;

    List<Item> getItemList(long userId);

    List<Item> getItemByText(String text);
}
