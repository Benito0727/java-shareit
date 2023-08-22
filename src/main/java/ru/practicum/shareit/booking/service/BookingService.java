package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {

    BookingDto addBooking(Long bookerId, IncomingBookingDto dto);

    BookingDto setApproved(Long ownerId, Long bookingId, Boolean isApproved);

    List<BookingDto> findAllByUserId(Long userId, String status, Integer from, Integer size);

    BookingDto findByBookingId(Long userId, Long bookingId);

    List<Booking> findAllByItemId(Long itemId);
}
