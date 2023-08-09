package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.booking.service.DBBookingService;

import java.util.Set;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final DBBookingService service;

    @Autowired
    public BookingController(DBBookingService service) {
        this.service = service;
    }

    @PostMapping
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @RequestBody @Valid IncomingBookingDto bookingDto) {
        return service.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto changeStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @PathVariable(value = "bookingId") Long bookingId,
                                           @RequestParam(value = "approved") Boolean isApproved) {
        return service.setApproved(userId, bookingId, isApproved);
    }

    @GetMapping
    public Set<BookingDto> getAllBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(value = "state", defaultValue = "all") String state) {
        return service.findAllByUserId(userId, state);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable(value = "bookingId") Long bookingId) {
        return service.findByBookingId(userId, bookingId);
    }

    @GetMapping("/owner")
    public Set<BookingDto> getAllForOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @RequestParam(value = "state", defaultValue = "all") String state) {
        return service.findBookingsToItemsOwner(userId, state);
    }
}
