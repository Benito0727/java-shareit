package ru.practicum.shareit.request.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.IncomingItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestDBService;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.Set;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestDBService service;

    @Autowired
    public ItemRequestController(ItemRequestDBService service) {
        this.service = service;
    }

    @PostMapping
    public ItemRequestDto addRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestBody @Valid IncomingItemRequestDto itemRequestDto) {
        return service.addRequest(userId, itemRequestDto);
    }

    @GetMapping
    public Set<ItemRequestDto> getUserRequest(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return service.getUserRequests(userId);
    }

    @GetMapping("/all")
    public Set<ItemRequestDto> getOtherUserRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @PathParam(value = "from") Long from,
                                                      @PathParam(value = "size") Long size) {
        return service.getOtherUsersRequests(userId, from, size);
    }

    @GetMapping("{requestId}")
    public ItemRequestDto getItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable(name = "requestId") Long requestId) {
        return service.getRequestById(userId, requestId);
    }
}
