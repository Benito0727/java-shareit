package ru.practicum.shareit.booking.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoEntityMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.QBooking;
import ru.practicum.shareit.booking.repository.DBBookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.item.repository.DBItemRepository;
import ru.practicum.shareit.user.repository.DBUserRepository;


import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DBBookingService implements BookingService {

    @Autowired
    private final DBBookingRepository storage;

    @Autowired
    private final DBItemRepository itemStorage;

    @Autowired
    private final DBUserRepository userStorage;

    @Override
    public BookingDto addBooking(Long userId, IncomingBookingDto dto) {
        try {
            userStorage.findById(userId).orElseThrow(
                    () -> new NotFoundException(String.format("Не нашли пользователя с ID: %d", userId))
            );
            Booking booking = BookingDtoEntityMapper.getEntityFromIncomingDto(dto);
            Item item = itemStorage.findById(booking.getItemId())
                            .orElseThrow(
                            () -> new NotFoundException(
                                    String.format("Не нашли вещь с ID: %d", booking.getItemId())
                            )
                    );
            booking.setBooker(userId);
            booking.setStatus(Status.WAITING);
            booking.setItemName(item.getName());
            return BookingDtoEntityMapper.getDtoFromEntity(storage.save(booking));
        } catch (NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public BookingDto changeStatus(Long ownerId, Long bookingId, Boolean isApproved) {
        try {

            Booking booking = storage.findById(bookingId).orElseThrow(
                    () -> new NotFoundException(String.format("Не нашли заявку с ID: %d", bookingId))
            );

            Item item = itemStorage.findById(booking.getItemId()).orElseThrow(
                    () -> new NotFoundException(String.format("Не нашли вещь с ID: %d", booking.getItemId()))
            );
            if (!item.getOwner().equals(ownerId)) {
                throw new ForbiddenException("Только владелец имеет доступ к этой функции");
            }
            if (isApproved) {
                booking.setStatus(Status.APPROVED);
            } else {
                booking.setStatus(Status.REJECTED);
            }
            return BookingDtoEntityMapper.getDtoFromEntity(storage.save(booking));
        } catch (NotFoundException | ForbiddenException  exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public Set<BookingDto> findAllByUserId(Long userId, String state) {
        try {
            switch (state.toLowerCase()) {
                case "all":
                    List<Booking> bookings = storage.findAllByBooker(userId);
                    return getDtoSetFromList(bookings);
                case "current":
                    return getCurrentBookingsByUserId(userId);
                case "past":
                case "waiting":
                case "rejected":
                    return getBookingsByUserIdAndStatus(userId, state.toLowerCase());
                case "future":
                    return getFutureBookingsByUserId(userId);
                default:
                    throw new NotFoundException(String.format("Команда: %s не реализована", state));
            }
        } catch (NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Transactional(readOnly = true)
    private Set<BookingDto> getCurrentBookingsByUserId(Long userId) {
        BooleanExpression byUserId = QBooking.booking.booker.eq(userId);
        BooleanExpression byStatus = QBooking.booking.status.eq(Status.APPROVED);
        BooleanExpression byStatDate = QBooking.booking.start.before(LocalDate.now());
        BooleanExpression byEndDate = QBooking.booking.end.after(LocalDate.now());



        Iterable<Booking> foundBookings = storage.findAll(byUserId.and(byStatus.and(byStatDate.and(byEndDate))));

        return getDtoSetFromIterable(foundBookings);
    }


    @Transactional(readOnly = true)
    private Set<BookingDto> getFutureBookingsByUserId(Long userId) {
        BooleanExpression byUserId = QBooking.booking.booker.eq(userId);
        BooleanExpression byStarDate = QBooking.booking.start.after(LocalDate.now());
        BooleanExpression byStatus = QBooking.booking.status.eq(Status.APPROVED);

        Iterable<Booking> foundBookings = storage.findAll(byUserId.and(byStarDate.and(byStatus)));

        return getDtoSetFromIterable(foundBookings);
    }

    @Transactional(readOnly = true)
    private Set<BookingDto> getBookingsByUserIdAndStatus(Long userId, String status) {
        BooleanExpression byUserId = QBooking.booking.booker.eq(userId);
        BooleanExpression byStatus = null;

        if (status.equals("past")) byStatus = QBooking.booking.status.eq(Status.PAST);
        if (status.equals("waiting")) byStatus = QBooking.booking.status.eq(Status.WAITING);
        if (status.equals("rejected")) byStatus = QBooking.booking.status.eq(Status.REJECTED);

        Iterable<Booking> foundBookings = storage.findAll(byUserId.and(byStatus));

        return getDtoSetFromIterable(foundBookings);
    }

    private Set<BookingDto> getDtoSetFromList(List<Booking> bookingList) {
        Set<BookingDto> dtoSet = new LinkedHashSet<>();
        for (Booking booking : bookingList) {
            dtoSet.add(BookingDtoEntityMapper.getDtoFromEntity(booking));
        }
        return dtoSet.stream()
                .sorted(Comparator.comparing(BookingDto::getStartDate))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Set<BookingDto> getDtoSetFromIterable(Iterable<Booking> bookings) {
        Set<BookingDto> dtoSet = new HashSet<>();
        for (Booking booking : bookings) {
            dtoSet.add(BookingDtoEntityMapper.getDtoFromEntity(booking));
        }
        return dtoSet.stream()
                .sorted(Comparator.comparing(BookingDto::getStartDate))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public BookingDto findByBookingId(Long bookingId) {
        try {
            return BookingDtoEntityMapper.getDtoFromEntity(
                    storage.findById(bookingId)
                            .orElseThrow(() -> new NotFoundException(String.format("Не нашли заявку с ID: %d", bookingId)))
            );
        } catch (NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public Set<BookingDto> findAllByItemId(Long itemId) {
        return getDtoSetFromList(storage.findAllByItemId(itemId));
    }
}
