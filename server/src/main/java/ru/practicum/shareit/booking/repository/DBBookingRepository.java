package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface DBBookingRepository extends JpaRepository<Booking, Long>, PagingAndSortingRepository<Booking, Long> {

    List<Booking> findAllByBookerId(Long id);

    List<Booking> findAllByItemId(Long id);

    List<Booking> findAllBookingsByItemId(Long id);

    List<Booking> findBookingsByItemIdAndBookerId(Long itemId,
                                                  Long bookerId);

    List<Booking> findAllByBookerIdAndStatusBetween(Long id,
                                                    Status status1,
                                                    Status status2);

    List<Booking> findAllByBookerIdAndStatus(Long id,
                                             Status status);

    List<Booking> findAllByBookerIdAndEndIsBefore(Long id,
                                                  LocalDateTime endTime);

    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfter(Long id,
                                                                 LocalDateTime startTime,
                                                                 LocalDateTime endTime);

    List<Booking> findAllByItemIdAndStatus(Long id,
                                           Status status);

    List<Booking> findAllByItemIdAndStatusIn(Long id,
                                             Collection<Status> status);

    List<Booking> findAllByItemIdAndStartIsBeforeAndEndIsAfter(Long id,
                                                               LocalDateTime startTime,
                                                               LocalDateTime endTime);

    List<Booking> findAllByItemIdAndEndBefore(Long id,
                                              LocalDateTime endTime);
}
