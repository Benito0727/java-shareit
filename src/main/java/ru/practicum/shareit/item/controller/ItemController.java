package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Set;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    @Autowired
    private ItemService service;

    @GetMapping
    public Set<Item> getItemSet(@RequestHeader("X-Sharer-User-Id") long userId) {
        return service.getItemSet(userId);
    }

    @GetMapping(value = "/{itemId}")
    public Item getItemById(@RequestHeader("X-Sharer-User-Id") long userId,
                            @PathVariable(value = "itemId") long itemId) {
        try {
            return service.getItemById(userId, itemId);
        } catch (NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    @PostMapping
    public Item addItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @RequestBody Item item) {
        try {
            return service.createItem(userId, item);
        } catch (ValidationException | NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    @PatchMapping(value = "/{itemId}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable(value = "itemId") long itemId,
                           @RequestBody Item item) {
        try {
            return service.updateItem(userId, itemId, item);
        } catch (ValidationException | NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    @DeleteMapping(value = "/{itemId}")
    public void removeItem(@RequestHeader("X-Sharer-User-id") long userId,
                           @PathVariable(value = "itemId") long itemId) {
        try {
            service.removeItemById(userId, itemId);
        } catch (NotFoundException | ValidationException exception) {
            throw new RuntimeException(exception);
        }
    }

    @GetMapping(value = "/search")
    public Set<Item> getSearch(@RequestHeader("X-Sharer-User-Id") long userId,
                               @RequestParam String text) {
        try {
            return service.getItemsByText(userId, text);
        }catch (NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }
}
