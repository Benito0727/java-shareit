package ru.practicum.shareit.booking.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    @PostMapping
    public IncomingBookingDto addBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestBody @Valid IncomingBookingDto bookingDto) {
        return null;
    }

    @PatchMapping("/{bookingId}")
    public IncomingBookingDto changeStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @PathVariable(value = "bookingId") Long bookingId,
                                           @RequestParam(value = "approved") Boolean isApproved) {
        return null;
    }

    @GetMapping
    public Set<IncomingBookingDto> getAllBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(value = "state", defaultValue = "All") String state) {
        return null;
    }
}
