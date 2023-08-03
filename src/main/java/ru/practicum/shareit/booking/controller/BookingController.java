package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.booking.service.DBBookingService;

import java.util.Set;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    @Autowired
    private final DBBookingService service;

    @PostMapping
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @RequestBody @Valid IncomingBookingDto bookingDto) {
        return service.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto changeStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @PathVariable(value = "bookingId") Long bookingId,
                                           @RequestParam(value = "approved") Boolean isApproved) {
        return service.changeStatus(userId, bookingId, isApproved);
    }

    @GetMapping
    public Set<BookingDto> getAllBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(value = "state", defaultValue = "all") String state) {
        return getAllBookings(userId, state);
    }
}
