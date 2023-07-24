package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import javax.validation.Valid;
import java.util.Set;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    @Autowired
    private final ItemServiceImpl service;

    @GetMapping
    public Set<Item> getItemSet(@RequestHeader("X-Sharer-User-Id") long userId) {
        return service.getItemSet(userId);
    }

    @GetMapping(value = "/{itemId}")
    public Item getItemById(@RequestHeader("X-Sharer-User-Id") long userId,
                            @PathVariable(value = "itemId") long itemId) {
        return service.getItemById(userId, itemId);
    }

    @PostMapping
    public Item addItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @RequestBody @Valid Item item) {
        return service.createItem(userId, item);
    }

    @PatchMapping(value = "/{itemId}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable(value = "itemId") long itemId,
                           @RequestBody Item item) {
        return service.updateItem(userId, itemId, item);
    }

    @DeleteMapping(value = "/{itemId}")
    public void removeItem(@RequestHeader("X-Sharer-User-id") long userId,
                           @PathVariable(value = "itemId") long itemId) {
        service.removeItemById(userId, itemId);
    }

    @GetMapping(value = "/search")
    public Set<Item> getSearch(@RequestHeader("X-Sharer-User-Id") long userId,
                               @RequestParam String text) {
        return service.getItemsByText(userId, text);
    }
}
