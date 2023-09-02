package ru.practicum.shareit.item.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.State;

import javax.validation.Valid;

@RestController
@RequestMapping("/items")
@Validated
@Slf4j
public class ItemController {

    private final ItemClient client;

    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @Autowired
    public ItemController(ItemClient client) {
        this.client = client;
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(X_SHARER_USER_ID) long userId,
                                          @RequestBody @Valid ItemDto dto) {
        log.info("userID:{} запрашивает добавление вещи item.name:{}",
                userId, dto.getName());
        return client.addItem(userId, dto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader(X_SHARER_USER_ID) long userId,
                                              @PathVariable(name = "itemId") long itemId) {
        log.info("userID:{} запрашивает itemId:{}",
                userId, itemId);
        return client.getItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader(X_SHARER_USER_ID) long userId,
                                           @RequestParam(value = "state", defaultValue = "all") String stringState,
                                           @RequestParam(value = "from", defaultValue = "0") Integer from,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("userID:{} запрашивает список вещей с state:{}, from:{}, size:{}",
                userId, stringState, from, size);
        return client.getItems(userId, getStateFromString(stringState), from, size);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(X_SHARER_USER_ID) long userId,
                                             @PathVariable("itemId") long itemId,
                                             @RequestBody ItemDto dto) {
        log.info("userID:{}, запрашивает обновление itemID:{} на item:{}",
                userId, itemId, dto);
        return client.updateItem(userId, itemId, dto);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(@RequestHeader(X_SHARER_USER_ID) long userId,
                                             @PathVariable("itemId") long itemId) {
        log.info("userID:{}, запрашивает удаление itemID:{}",
                userId, itemId);
        return client.deleteItem(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemByText(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                @RequestParam String text,
                                                @RequestParam(value = "state", defaultValue = "all") String stringState,
                                                @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                @RequestParam(value = "size", defaultValue = "10") Integer size) {

        log.info("userID:{} запрашивает поиск по text:{} с state:{}, from:{}, size:{}",
                userId, text, stringState, from, size);
        return client.getItemsByText(userId, text, getStateFromString(stringState), from, size);
    }

    @PostMapping("/{itemId}/comments")
    public ResponseEntity<Object> addComment(@RequestHeader(X_SHARER_USER_ID) long userId,
                                             @PathVariable("itemId") long itemId,
                                             @RequestBody @Valid CommentDto dto) {
        log.info("userID:{}, запрашивает добавление комментария:{} к itemID:{}",
                userId, dto, itemId);
        return client.addComment(userId, itemId, dto);
    }

    private State getStateFromString(String stringState) {
        try {
            return State.getStateFrom(stringState)
                    .orElseThrow(() -> new BadRequestException("Unknown state: " + stringState));
        } catch (BadRequestException exception) {
            throw new RuntimeException(exception);
        }
    }
}
