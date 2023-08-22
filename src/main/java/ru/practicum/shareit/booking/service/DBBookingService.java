package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoEntityMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.DBBookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.repository.DBItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.DBUserRepository;

import java.time.LocalDateTime;
import java.util.*;
@Service
public class DBBookingService implements BookingService {

    private final DBBookingRepository storage;

    private final DBItemRepository itemStorage;

    private final DBUserRepository userStorage;

    @Autowired
    public DBBookingService(DBBookingRepository storage, DBItemRepository itemStorage, DBUserRepository userStorage) {
        this.storage = storage;
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    @Override
    @Transactional
    public BookingDto addBooking(Long userId, IncomingBookingDto dto) {
        try {
            User user = checkUser(userId);
            Booking booking = BookingDtoEntityMapper.getEntityFromIncomingDto(dto);
            if (booking.getStart().toLocalDate().isAfter(booking.getEnd().toLocalDate()) ||
                booking.getStart().equals(booking.getEnd()) ||
                booking.getStart().toLocalDate().isBefore(LocalDateTime.now().toLocalDate())) {
                throw new ValidationException("Дата старта должна быть в будущем" +
                        " и дата окончания должна быть позже даты старта");
            }
            Item item = itemStorage.findById(dto.getItemId())
                            .orElseThrow(
                            () -> new NotFoundException(
                                    String.format("Не нашли вещь с ID: %d", dto.getItemId())
                            )
                    );
            if (item.getOwner().getId() == userId) {
                throw new NotFoundException("Владелец хочет создать заявку на свою вещь");
            }
            if (item.getAvailable()) {
                booking.setBooker(user);
                booking.setStatus(Status.WAITING);
                booking.setItem(item);
                return BookingDtoEntityMapper.getDtoFromEntity(storage.save(booking), item);
            } else {
                throw new BadRequestException(String.format("Вещь с ID: %d недоступна", item.getId()));
            }
        } catch (ValidationException | NotFoundException | BadRequestException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    @Transactional
    public BookingDto setApproved(Long userId, Long bookingId, Boolean isApproved) {
        try {
            checkUser(userId);

            Booking booking = storage.findById(bookingId).orElseThrow(
                    () -> new NotFoundException(String.format("Не нашли заявку с ID: %d", bookingId))
            );

            Item item = itemStorage.findById(booking.getItem().getId()).orElseThrow(
                    () -> new NotFoundException(String.format("Не нашли вещь с ID: %d", booking.getItem().getId()))
            );
            if (item.getOwner().getId() != userId) {
                throw new NotFoundException("Только владелец имеет доступ к этой функции");
            }
            if (booking.getStatus().equals(Status.APPROVED) && isApproved) throw new BadRequestException();
            if (isApproved) {
                booking.setStatus(Status.APPROVED);
            } else {
                booking.setStatus(Status.REJECTED);
            }
            return BookingDtoEntityMapper.getDtoFromEntity(storage.save(booking), item);
        } catch (NotFoundException | BadRequestException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    @Transactional
    public List<BookingDto> findAllByUserId(Long userId, String state, Integer from, Integer size) {
        try {
           checkUser(userId);
            switch (state.toLowerCase()) {
                case "all":
                    return getDtoList(storage.findAllByBookerId(userId,
                            PageRequest.of(from,
                                    size,
                                    Sort.by("start").descending())));
                case "current":
                    return getDtoList(
                      storage.findAllByBookerIdAndStartIsBeforeAndEndIsAfter(userId,
                              LocalDateTime.now(),
                              LocalDateTime.now(),
                              PageRequest.of(from,
                                      size,
                                      Sort.by("start").descending()))
                    );
                case "past":
                    return getDtoList(
                            storage.findAllByBookerIdAndEndIsBefore(userId,
                                    LocalDateTime.now(),
                                    PageRequest.of(from,
                                            size,
                                            Sort.by("start").descending()))
                    );
                case "waiting":
                    return getDtoList(
                            storage.findAllByBookerIdAndStatus(userId,
                                    Status.WAITING,
                                    PageRequest.of(from,
                                            size,
                                            Sort.by("start").descending()))
                    );
                case "rejected":
                    return getDtoList(
                            storage.findAllByBookerIdAndStatus(userId,
                                    Status.REJECTED,
                                    PageRequest.of(from,
                                            size,
                                            Sort.by("start").descending()))
                    );
                case "future":
                    return getDtoList(
                            storage.findAllByBookerIdAndStatusBetween(userId, Status.WAITING,
                                    Status.APPROVED,
                                    PageRequest.of(from,
                                            size,
                                            Sort.by("start").descending()))
                    );
                default:
                    throw new BadRequestException(String.format("Unknown state: %s", state));
            }
        } catch (BadRequestException exception) {
            throw new RuntimeException(exception);
        }
    }

    public List<BookingDto> findBookingsToItemsOwner(Long ownerId, String state, Integer from, Integer size) {
        try {
            checkUser(ownerId);
            List<Item> items = itemStorage.findByOwnerId(ownerId);
            List<Booking> bookings = new ArrayList<>();
            switch (state.toLowerCase()) {
                case "all":
                    for (Item item : items) {
                        bookings.addAll(storage.findAllBookingsByItemId(item.getId(),
                                PageRequest.of(from,
                                        size,
                                        Sort.by("start").descending())));
                    }
                    return getDtoList(bookings);
                case "current":
                    for (Item item : items) {
                        bookings.addAll(
                                storage.findAllByItemIdAndStartIsBeforeAndEndIsAfter(item.getId(),
                                        LocalDateTime.now(),
                                        LocalDateTime.now(),
                                        PageRequest.of(from,
                                                        size,
                                                         Sort.by("start").descending()))
                        );
                    }
                    return getDtoList(bookings);
                case "past":
                    for (Item item : items) {
                        bookings.addAll(storage.findAllByItemIdAndEndBefore(item.getId(),
                                LocalDateTime.now(),
                                PageRequest.of(from,
                                        size,
                                        Sort.by("start").descending())));
                    }
                    return getDtoList(bookings);
                case "rejected":
                    for (Item item : items) {
                        bookings.addAll(storage.findAllByItemIdAndStatus(item.getId(),
                                Status.REJECTED,
                                PageRequest.of(from,
                                        size,
                                        Sort.by("start").descending())));
                    }
                    return getDtoList(bookings);
                case "future":
                    for (Item item : items) {
                        bookings.addAll(storage.findAllByItemIdAndStatusIn(
                                item.getId(),
                                List.of(Status.WAITING, Status.APPROVED),
                                PageRequest.of(from,
                                        size,
                                        Sort.by("start").descending()))
                        );
                    }
                    return getDtoList(bookings);
                case "waiting":
                    for (Item item : items) {
                        bookings.addAll(storage.findAllByItemIdAndStatus(item.getId(),
                                Status.WAITING,
                                PageRequest.of(from,
                                        size,
                                        Sort.by("start").descending())));
                    }
                    return getDtoList(bookings);
                default:
                    throw new BadRequestException(String.format("Unknown state: %s", state));
            }
        } catch (BadRequestException exception) {
            throw new RuntimeException(exception);
        }
    }

    private List<BookingDto> getDtoList(List<Booking> bookingList) {
        List<BookingDto> dtoSet = new LinkedList<>();
        for (Booking booking : bookingList) {
            dtoSet.add(
                    BookingDtoEntityMapper.getDtoFromEntity(
                            booking,
                            itemStorage.findById(booking.getItem().getId()).orElseThrow())
            );
        }
        return dtoSet;
    }

    @Override
    @Transactional
    public BookingDto findByBookingId(Long userId, Long bookingId) {
        try {
            checkUser(userId);
            Booking booking = storage.findById(bookingId)
                    .orElseThrow(
                            () -> new NotFoundException(String.format("Не нашли заявку с ID: %d", bookingId))
                    );
            Item item = itemStorage.findById(booking.getItem().getId()).orElseThrow(
                    () -> new NotFoundException(String.format("Не нашли вещь с ID: %d", booking.getItem().getId()))
            );
            if (booking.getBooker().getId() != userId) {
                if (item.getOwner().getId() != userId) {
                    throw new NotFoundException("Эта заявка тебе не принадлежит");
                }
            }
            return BookingDtoEntityMapper.getDtoFromEntity(
                    booking,
                    item
            );
        } catch (NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    @Transactional
    public List<Booking> findAllByItemId(Long itemId) {
        return storage.findAllByItemId(itemId);
    }

    private User checkUser(Long userId) {
        try {
            return userStorage.findById(userId).orElseThrow(
                    () -> new NotFoundException(String.format("Не нашли пользователя с ID: %d", userId))
            );
        } catch (NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }
}
