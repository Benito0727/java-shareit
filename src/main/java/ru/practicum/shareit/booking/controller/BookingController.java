package ru.practicum.shareit.booking.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.booking.service.DBBookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final DBBookingService service;

    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @Autowired
    public BookingController(DBBookingService service) {
        this.service = service;
    }

    @PostMapping
    public BookingDto addBooking(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                 @RequestBody @Valid IncomingBookingDto bookingDto) {
        return service.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto changeStatus(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                           @PathVariable(value = "bookingId") Long bookingId,
                                           @RequestParam(value = "approved") Boolean isApproved) {
        return service.setApproved(userId, bookingId, isApproved);
    }

    @GetMapping
    public List<BookingDto> getAllBookings(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                           @RequestParam(value = "state", defaultValue = "all") String state,
                                           @RequestParam(value = "from", defaultValue = "0") Integer from,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return service.findAllByUserId(userId, state, from, size).getPageList();
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                     @PathVariable(value = "bookingId") Long bookingId) {
        return service.findByBookingId(userId, bookingId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllForOwner(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                      @RequestParam(value = "state", defaultValue = "all") String state,
                                                      @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return service.findBookingsToItemsOwner(userId, state, from, size).getPageList();
    }
}
