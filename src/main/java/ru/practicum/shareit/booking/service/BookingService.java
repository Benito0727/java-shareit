package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;

import java.util.Set;

public interface BookingService {

    Booking addBooking(Booking booking);

    Booking changeStatus(Long ownerId, Long bookingId, Boolean isApproved);

    Set<Booking> findAllByUserId(Long userId, String status);
}
