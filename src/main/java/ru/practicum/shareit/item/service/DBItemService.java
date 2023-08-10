package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookings;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.DBItemRepository;
import ru.practicum.shareit.item.dto.ItemEntityDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.DBUserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DBItemService implements ItemService {

    private final DBItemRepository storage;

    private final DBUserRepository userStorage;

    @Autowired
    public DBItemService(DBItemRepository storage, DBUserRepository userStorage) {
        this.storage = storage;
        this.userStorage = userStorage;
    }

    @Override
    public ItemDto createItem(long userId, ItemDto itemDto) {
        try {
            User user = userStorage.findById(userId)
                    .orElseThrow(
                            () -> new NotFoundException(String.format("Не нашли пользователя с ID: %d", userId))
                    );
            Item item = ItemEntityDtoMapper.getItemFromItemDto(itemDto);
            item.setOwner(user);

            return ItemEntityDtoMapper.getItemDtoFromItem(storage.save(item));

        } catch (NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        try {
            userStorage.findById(userId)
                    .orElseThrow(() -> new NotFoundException(String.format("Не нашли пользователя с ID: %d", userId)));
            Item item = storage.findById(itemId)
                    .orElseThrow(() -> new NotFoundException(String.format("Не нашли вещи с ID: %d", itemId)));
            item.setId(itemId);
            if (itemDto.getName() != null) item.setName(itemDto.getName());
            if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());
            if (itemDto.getAvailable() != null) item.setAvailable(itemDto.getAvailable());

            return ItemEntityDtoMapper.getItemDtoFromItem(storage.save(item));
        } catch (NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public ItemDtoWithBookings getItemById(long userId, long itemId) {
        try {
            Item item = storage.findById(itemId).
                    orElseThrow(() -> new NotFoundException(String.format("Не нашли вещи с ID: %d", itemId)));

            return ItemEntityDtoMapper.getItemDtoWithBookings(findNearestBookingsForItem(item));
        } catch (NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void removeItemById(long userId, long itemId) {
        try {
        storage.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Не нашли вещи с ID: %d", itemId)));
        } catch (NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public Set<ItemDtoWithBookings> getItemSet(long userId) {
        List<Item> itemList = storage.findByOwnerId(userId);
        Set<ItemDtoWithBookings> itemSet = new LinkedHashSet<>();
        for (Item item : itemList) {
            itemSet.add(ItemEntityDtoMapper.getItemDtoWithBookings(findNearestBookingsForItem(item)));
        }
        return itemSet.stream()
                .sorted((o1, o2) -> (int) (o1.getId() - o2.getId()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Item findNearestBookingsForItem(Item item) {
        Set<Booking> bookings = item.getBookings();
        bookings.stream()
                .filter(booking -> booking.getStatus().equals(Status.PAST))
                .max(Comparator.comparing(Booking::getEnd))
                .ifPresent(lastBooking -> item.setLastBookingId(lastBooking.getId()));

        bookings.stream()
                .filter(booking -> booking.getStatus().equals(Status.APPROVED))
                .min(Comparator.comparing(Booking::getStart))
                .ifPresent(nextBooking -> item.setNextBookingId(nextBooking.getId()));
        return item;
    }

    @Override
    public Set<ItemDtoWithBookings> getItemsByText(long userId, String text) {
        if (text.isEmpty()) return Set.of();
        List<Item> itemList = storage.findByNameContainingOrDescriptionContainingIgnoreCase(text, text);
        Set<ItemDtoWithBookings> itemDtoSet = new HashSet<>();
        for (Item item : itemList) {
            itemDtoSet.add(ItemEntityDtoMapper.getItemDtoWithBookings(findNearestBookingsForItem(item)));
        }

        return itemDtoSet.stream()
                .sorted((o1, o2) -> (int) (o1.getId() - o2.getId()))
                .filter(o -> o.getAvailable().equals(true))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
