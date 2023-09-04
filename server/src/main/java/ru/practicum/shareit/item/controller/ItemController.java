package ru.practicum.shareit.item.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.comment.IncomingCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.DBCommentService;
import ru.practicum.shareit.item.service.DBItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final DBItemService service;

    private final DBCommentService commentService;

    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private long userId;
    private String text;
    private Integer from;
    private Integer size;

    @Autowired
    public ItemController(DBItemService service, DBCommentService commentService) {
        this.service = service;
        this.commentService = commentService;
    }

    @GetMapping
    public List<ItemDto> getItemSet(@RequestHeader(X_SHARER_USER_ID) long userId,
                                    @RequestParam(value = "from", defaultValue = "0") Integer from,
                                    @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return service.getItems(userId, from, size);
    }

    @GetMapping(value = "/{itemId}")
    public ItemDto getItemById(@RequestHeader(X_SHARER_USER_ID) long userId,
                            @PathVariable(value = "itemId") long itemId) {
        return service.getItemById(userId, itemId);
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader(X_SHARER_USER_ID) long userId,
                           @RequestBody ItemDto itemDto) {
        return service.createItem(userId, itemDto);
    }

    @PatchMapping(value = "/{itemId}")
    public ItemDto updateItem(@RequestHeader(X_SHARER_USER_ID) long userId,
                           @PathVariable(value = "itemId") long itemId,
                           @RequestBody ItemDto itemDto) {
        return service.updateItem(userId, itemId, itemDto);
    }

    @DeleteMapping(value = "/{itemId}")
    public void removeItem(@RequestHeader(X_SHARER_USER_ID) long userId,
                           @PathVariable(value = "itemId") long itemId) {
        service.removeItemById(userId, itemId);
    }

    @GetMapping(value = "/search")
    public List<ItemDto> getSearch(@RequestHeader(X_SHARER_USER_ID) long userId,
                                       @RequestParam(value = "text", defaultValue = "")  String text,
                                       @RequestParam(value = "from", defaultValue = "0") Integer from,
                                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        this.userId = userId;
        this.text = text;
        this.from = from;
        this.size = size;
        return service.getItemsByText(userId, text, from, size);
    }

    @PostMapping(value = "/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(X_SHARER_USER_ID) long userId,
                                 @PathVariable(value = "itemId") long itemId,
                                 @RequestBody IncomingCommentDto comment) {
        return commentService.addComment(userId, itemId, comment);
    }
}
