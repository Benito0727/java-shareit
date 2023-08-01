package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.DBItemService;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    @Autowired
    private final DBItemService service;

    @GetMapping
    public Set<ItemDto> getItemSet(@RequestHeader("X-Sharer-User-Id") long userId) {
        return service.getItemSet(userId);
    }

    @GetMapping(value = "/{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") long userId,
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
    public Set<ItemDto> getSearch(@RequestHeader("X-Sharer-User-Id") long userId,
                               @RequestParam String text) {
        return service.getItemsByText(userId, text);
    }
}
