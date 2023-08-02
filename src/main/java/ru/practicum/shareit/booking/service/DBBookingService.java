package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;

import java.util.Set;

public class DBBookingService implements BookingService {



    @Override
    public Booking addBooking(Booking booking) {
        return null;
    }

    @Override
    public Booking changeStatus(Long ownerId, Long bookingId, Boolean isApproved) {
        return null;
    }

    @Override
    public Set<Booking> findAllByUserId(Long userId, String status) {
        return null;
    }
}
