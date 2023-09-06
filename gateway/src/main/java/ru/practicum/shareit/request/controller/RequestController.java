package ru.practicum.shareit.request.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.request.client.RequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.State;

import javax.validation.Valid;

@RestController
@RequestMapping("/requests")
@Validated
@Slf4j
public class RequestController {

    private final RequestClient client;

    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @Autowired
    public RequestController(RequestClient client) {
        this.client = client;
    }

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader(X_SHARER_USER_ID) long userId,
                                             @RequestBody @Valid ItemRequestDto dto) {
        log.info("userID:{} запросил создание нового реквеста с description:{}",
                userId, dto.getDescription());
        return client.addRequest(userId, dto);
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(@RequestHeader(X_SHARER_USER_ID) long userId,
            @RequestParam(name = "state", defaultValue = "all") String stringState,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {

        log.info("userID:{} запросил свои реквесты с state:{}, from:{}, size:{}",
                userId, stringState, from, size);
        return client.getUserRequests(userId, getStateFromString(stringState), from, size);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getOtherUsersRequests(@RequestHeader(X_SHARER_USER_ID) long userId,
            @RequestParam(name = "state", defaultValue = "all") String stringState,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {

        log.info("userID:{} запросил реквесты других пользователей с state:{}, from:{}, size:{}",
                userId, stringState, from, size);
        return client.getOtherUserRequests(userId, getStateFromString(stringState), from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                 @PathVariable("requestId") long requestId) {
        log.info("userID:{} запросил реквест с requestId:{}", userId, requestId);
        return client.getRequestById(userId, requestId);
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
