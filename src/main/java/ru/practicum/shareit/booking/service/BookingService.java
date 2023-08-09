package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;

import java.util.Set;

public interface BookingService {

    BookingDto addBooking(Long bookerId, IncomingBookingDto dto);

    BookingDto setApproved(Long ownerId, Long bookingId, Boolean isApproved);

    Set<BookingDto> findAllByUserId(Long userId, String status);

    BookingDto findByBookingId(Long userId, Long bookingId);

    Set<BookingDto> findAllByItemId(Long itemId);
}
