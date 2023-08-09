package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DBBookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerId(Long id);

    List<Booking> findAllByItemId(Long id);

    List<Booking> findAllByBookerIdAndStatusBetween(Long id, Status status1, Status status2);

    List<Booking> findAllByBookerIdAndStatus(Long id, Status status);

    List<Booking> findAllByBookerIdAndStatusAndEndIsAfter(Long id, Status status, LocalDateTime end);

    List<Booking> findAllByItemIdAndStatus(Long id, Status status);

    List<Booking> findAllByItemIdAndStatusBetween(Long id, Status status1, Status status2);
}
