package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookings;
import ru.practicum.shareit.item.service.DBItemService;

import java.util.Set;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final DBItemService service;

    @Autowired
    public ItemController(DBItemService service) {
        this.service = service;
    }

    @GetMapping
    public Set<ItemDtoWithBookings> getItemSet(@RequestHeader("X-Sharer-User-Id") long userId) {
        return service.getItemSet(userId);
    }

    @GetMapping(value = "/{itemId}")
    public ItemDtoWithBookings getItemById(@RequestHeader("X-Sharer-User-Id") long userId,
                            @PathVariable(value = "itemId") long itemId) {
        return service.getItemById(userId, itemId);
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @RequestBody @Valid ItemDto itemDto) {
        return service.createItem(userId, itemDto);
    }

    @PatchMapping(value = "/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable(value = "itemId") long itemId,
                           @RequestBody ItemDto itemDto) {
        return service.updateItem(userId, itemId, itemDto);
    }

    @DeleteMapping(value = "/{itemId}")
    public void removeItem(@RequestHeader("X-Sharer-User-id") long userId,
                           @PathVariable(value = "itemId") long itemId) {
        service.removeItemById(userId, itemId);
    }

    @GetMapping(value = "/search")
    public Set<ItemDtoWithBookings> getSearch(@RequestHeader("X-Sharer-User-Id") long userId,
                               @RequestParam String text) {
        return service.getItemsByText(userId, text);
    }
}
