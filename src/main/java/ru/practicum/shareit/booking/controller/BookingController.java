package ru.practicum.shareit.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.booking.service.DBBookingService;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final DBBookingService service;

    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @Autowired
    public BookingController(DBBookingService service) {
        this.service = service;
    }
}
