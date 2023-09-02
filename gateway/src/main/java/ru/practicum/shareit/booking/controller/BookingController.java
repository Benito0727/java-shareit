package ru.practicum.shareit.booking.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.exception.BadRequestException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/bookings")
@Slf4j
@Validated
public class BookingController {

    private final BookingClient client;

    private static final java.lang.String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @Autowired
    public BookingController(BookingClient client) {
        this.client = client;
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookings(@RequestHeader(X_SHARER_USER_ID) long userId,
            @RequestParam(name = "state", defaultValue = "all") String stringState,
            @PositiveOrZero @RequestParam(value = "size", defaultValue = "10") Integer size,
            @Positive @RequestParam(value = "from", defaultValue = "0") Integer from) {

        log.info("Запрашиваем букинги с state={}, userID={}, from={}, size={}",
                stringState, userId, from, size);
        return client.getAllBookings(userId, getStateFromString(stringState), from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsForOwner(@RequestHeader(X_SHARER_USER_ID) long userId,
            @RequestParam(name = "state", defaultValue = "all") String stringState,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {

        log.info("Запрашиваем букинги от лица владельца userID={}, state={}, from={}, size={}",
                userId, stringState, from, size);
        return client.getAllBookingsForOwner(userId, getStateFromString(stringState), from, size);
    }

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader(X_SHARER_USER_ID) long userId,
                                             @RequestBody @Valid BookingDto dto) {
        log.info("Создаем букинг {} от userID={}", dto, userId);
        return client.addBooking(userId, dto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(X_SHARER_USER_ID) long userId,
                                             @PathVariable("bookingId") Long bookingId) {
        log.info("Запросили букинг с bookingID={} от userID={}", bookingId, userId);
        return client.getBookingById(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> setApproved(@RequestHeader(X_SHARER_USER_ID) long userId,
                                              @PathVariable("bookingId") Long bookingId,
                                              @RequestParam(value = "approved") Boolean isApproved) {
        log.info("Запросили подтверждение bookingID={} от userID={}, approved={}",
                bookingId, userId, isApproved);
        return client.setApproved(userId, bookingId, isApproved);
    }

    private State getStateFromString(String stringState) {
        try {
            return State.getStateFrom(stringState)
                    .orElseThrow(() -> new BadRequestException("Unknown state :" + stringState));
        } catch (BadRequestException exception) {
            throw new RuntimeException(exception);
        }
    }
}
