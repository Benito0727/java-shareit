package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
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
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.dto.BookingDtoEntityMapper.getDtoFromEntity;


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
            if (booking.getStart().isAfter(booking.getEnd()) ||
                booking.getStart().equals(booking.getEnd()) ||
                booking.getStart().isBefore(LocalDateTime.now())) {
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
                return getDtoFromEntity(storage.save(booking), item);
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
            return getDtoFromEntity(storage.save(booking), item);
        } catch (NotFoundException | BadRequestException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    @Transactional
    public PagedListHolder<BookingDto> findAllByUserId(Long userId, String state, Integer from, Integer size) {
        try {
           checkUser(userId);
            switch (state.toLowerCase()) {
                case "all":
                    return getPagedDtoList(storage.findAllByBookerId(userId),
                            from,
                            size);
                case "current":
                    return getPagedDtoList(
                      storage.findAllByBookerIdAndStartIsBeforeAndEndIsAfter(userId,
                              LocalDateTime.now(),
                              LocalDateTime.now()),
                            from,
                            size
                    );
                case "past":
                    return getPagedDtoList(
                            storage.findAllByBookerIdAndEndIsBefore(userId,
                                    LocalDateTime.now()),
                            from,
                            size
                    );
                case "waiting":
                    return getPagedDtoList(
                            storage.findAllByBookerIdAndStatus(userId,
                                    Status.WAITING),
                            from,
                            size
                    );
                case "rejected":
                    return getPagedDtoList(
                            storage.findAllByBookerIdAndStatus(userId,
                                    Status.REJECTED),
                                    from,
                                    size
                    );
                case "future":
                    return getPagedDtoList(
                            storage.findAllByBookerIdAndStatusBetween(userId, Status.WAITING,
                                    Status.APPROVED),
                            from,
                            size
                    );
                default:
                    throw new BadRequestException(String.format("Unknown state: %s", state));
            }
        } catch (BadRequestException exception) {
            throw new RuntimeException(exception);
        }
    }

    public PagedListHolder<BookingDto> findBookingsToItemsOwner(Long ownerId, String state, Integer from, Integer size) {
        try {
            checkUser(ownerId);
            List<Item> items = itemStorage.findByOwnerId(ownerId);
            List<Booking> bookings = new ArrayList<>();
            switch (state.toLowerCase()) {
                case "all":
                    for (Item item : items) {
                        bookings.addAll(storage.findAllBookingsByItemId(item.getId()));
                    }
                    return getPagedDtoList(bookings, from, size);
                case "current":
                    for (Item item : items) {
                        bookings.addAll(
                                storage.findAllByItemIdAndStartIsBeforeAndEndIsAfter(item.getId(),
                                        LocalDateTime.now(),
                                        LocalDateTime.now())
                        );
                    }
                    return getPagedDtoList(bookings, from, size);
                case "past":
                    for (Item item : items) {
                        bookings.addAll(storage.findAllByItemIdAndEndBefore(item.getId(),
                                LocalDateTime.now()));
                    }
                    return getPagedDtoList(bookings, from, size);
                case "rejected":
                    for (Item item : items) {
                        bookings.addAll(storage.findAllByItemIdAndStatus(item.getId(),
                                Status.REJECTED));
                    }
                    return getPagedDtoList(bookings,from, size);
                case "future":
                    for (Item item : items) {
                        bookings.addAll(storage.findAllByItemIdAndStatusIn(
                                item.getId(),
                                List.of(Status.WAITING, Status.APPROVED))
                        );
                    }
                    return getPagedDtoList(bookings, from, size);
                case "waiting":
                    for (Item item : items) {
                        bookings.addAll(storage.findAllByItemIdAndStatus(item.getId(),
                                Status.WAITING));
                    }
                    return getPagedDtoList(bookings, from, size);
                default:
                    throw new BadRequestException(String.format("Unknown state: %s", state));
            }
        } catch (BadRequestException exception) {
            throw new RuntimeException(exception);
        }
    }

    private PagedListHolder<BookingDto> getPagedDtoList(List<Booking> bookingList, int from, int size) {
        try {
            if (size < 1) throw new BadRequestException("Некорректные параметры страницы 'size': " + size);
            if (from < 0) throw new BadRequestException("Некорректные параметры страницы 'from': " + from);
            List<BookingDto> dtoList;

            dtoList = bookingList.stream()
                    .sorted(Comparator.comparing(Booking::getStart).reversed())
                    .sorted(Comparator.comparing(Booking::getEnd).reversed())
                    .map(booking -> BookingDtoEntityMapper.getDtoFromEntity(booking,
                            itemStorage.findById(booking.getItem().getId()).orElseThrow()))
                    .collect(Collectors.toCollection(LinkedList::new));

            PagedListHolder<BookingDto> pageHolder = new PagedListHolder<>(dtoList);
            pageHolder.setPage(from);
            pageHolder.setMaxLinkedPages(size);
            if (pageHolder.getPageSize() > size) pageHolder.setPageSize(size);
            return pageHolder;
        } catch (BadRequestException exception) {
            throw new RuntimeException(exception);
        }
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
            return getDtoFromEntity(
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
