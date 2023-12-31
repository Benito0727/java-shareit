package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoToItem;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.DBBookingService;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.DBCommentRepository;
import ru.practicum.shareit.item.repository.DBItemRepository;
import ru.practicum.shareit.item.dto.ItemEntityDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.DBUserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.dto.BookingDtoEntityMapper.getBookingDtoToItem;

@Service
public class DBItemService implements ItemService {

    private final DBItemRepository storage;

    private final DBUserRepository userStorage;

    private final DBBookingService bookingService;

    private final DBCommentService commentService;

    private final DBCommentRepository commentStorage;

    @Autowired
    public DBItemService(DBItemRepository storage, DBUserRepository userStorage, DBBookingService dbBookingService, DBCommentService commentService, DBCommentRepository commentStorage) {
        this.storage = storage;
        this.userStorage = userStorage;
        this.bookingService = dbBookingService;
        this.commentService = commentService;
        this.commentStorage = commentStorage;
    }

    @Override
    public ItemDto createItem(long userId, ItemDto itemDto) {
        User user = checkUser(userId);
        Item item = ItemEntityDtoMapper.getItemFromItemDto(itemDto);
        item.setOwner(user);

        if (itemDto.getRequestId() != null) {
            item.setRequestId(itemDto.getRequestId());
        }

        return ItemEntityDtoMapper.getItemDtoFromItem(storage.save(item));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        try {
            User user = checkUser(userId);
            Item item = checkItem(itemId);

            if (item.getOwner() != user) {
                throw new BadRequestException(("Только владелец может обновить информацию о вещи"));
            }

            if (itemDto.getName() != null) item.setName(itemDto.getName());
            if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());
            if (itemDto.getAvailable() != null) item.setAvailable(itemDto.getAvailable());

            return ItemEntityDtoMapper.getItemDtoFromItem(storage.save(item));
        } catch (BadRequestException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public ItemDto getItemById(long userId, long itemId) {
        Item item = checkItem(itemId);
        checkUser(userId);
        ItemDto itemDto = ItemEntityDtoMapper.getItemDtoFromItem(item);

        if (item.getOwner().getId() == userId) {
            itemDto = setLastNextBookingsToItem(item);
        }

        itemDto = commentService.setCommentsToItems(itemDto);

        return itemDto;
    }

    @Override
    public void removeItemById(long userId, long itemId) {
        try {
            checkUser(userId);
            Item item = checkItem(itemId);
            if (item.getOwner().getId() == userId) {
                storage.delete(item);
                commentStorage.deleteCommentsByItemId(itemId);
            } else {
                throw new BadRequestException("Только владелец может удалить вещь");
            }
        } catch (BadRequestException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public List<ItemDto> getItems(long userId, Integer from, Integer size) {
        checkUser(userId);
        List<Item> itemList = storage.findItemsByOwnerId(userId,
                PageRequest.of(from,
                        size,
                        Sort.by("id")));
        List<ItemDto> itemSet = new LinkedList<>();

        for (Item item : itemList) {
            itemSet.add(setLastNextBookingsToItem(item));
        }

        for (ItemDto itemDto : itemSet) {
            commentService.setCommentsToItems(itemDto);
        }

        return itemSet.stream()
                .sorted((o1, o2) -> (int) (o1.getId() - o2.getId()))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private ItemDto setLastNextBookingsToItem(Item item) {
        findNearestBookingsForItem(item);
        ItemDto itemDto = ItemEntityDtoMapper.getItemDtoFromItem(item);
        BookingDtoToItem bookingDtoToItem;
        if (item.getLastBooking() != null) {
            bookingDtoToItem = getBookingDtoToItem(
                    bookingService.findByBookingId(item.getOwner().getId(), item.getLastBooking())
            );
            itemDto.setLastBooking(bookingDtoToItem);
        }
        if (item.getNextBooking() != null) {
            bookingDtoToItem = getBookingDtoToItem(
                    bookingService.findByBookingId(item.getOwner().getId(), item.getNextBooking())
            );
            itemDto.setNextBooking(bookingDtoToItem);
        }

        if (itemDto.getLastBooking() == null) {
            itemDto.setLastBooking(itemDto.getNextBooking());
            itemDto.setNextBooking(null);
        }

        return itemDto;
    }

    private void findNearestBookingsForItem(Item item) {
        List<Booking> bookings = bookingService.findAllByItemId(item.getId());
        bookings.stream()
                .filter(booking -> booking.getStatus().equals(Status.APPROVED) &&
                        booking.getEnd().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(Booking::getStart))
                .ifPresent(booking -> item.setNextBooking(booking.getId()));

        bookings.stream()
                .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()) &&
                        booking.getStatus().equals(Status.APPROVED))
                .max(Comparator.comparing(Booking::getEnd))
                .ifPresent(booking -> item.setLastBooking(booking.getId()));
    }

    @Override
    public List<ItemDto> getItemsByText(long userId, String text, Integer from, Integer size) {
        checkUser(userId);
        if (text.isEmpty()) return List.of();
        int page = from / size;
        List<Item> itemList = storage.findByNameContainingOrDescriptionContainingIgnoreCase(text,
                text,
                PageRequest.of(page,
                        size,
                        Sort.by("id")));

        Set<ItemDto> itemDtoSet = new HashSet<>();
        for (Item item : itemList) {
            itemDtoSet.add(setLastNextBookingsToItem(item));
        }

        return itemDtoSet.stream()
                .sorted((o1, o2) -> (int) (o1.getId() - o2.getId()))
                .filter(o -> o.getAvailable().equals(true))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private Item checkItem(Long itemId) {
        try {
            return storage.findById(itemId).orElseThrow(
                    () -> new NotFoundException(String.format("Не нашли вещи с ID: %d", itemId))
            );
        } catch (NotFoundException exception) {
            throw new RuntimeException(exception);
        }
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
