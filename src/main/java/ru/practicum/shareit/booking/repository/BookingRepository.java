package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingRepository {

    Booking addBooking(Booking booking);

    Booking changeStatus(Long userId, Long bookingId, Boolean isApproved);

    List<Booking> findAllByUserId(Long userId, String status);
}
