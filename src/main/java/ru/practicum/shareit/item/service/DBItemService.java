package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.DBItemRepository;
import ru.practicum.shareit.item.dto.ItemEntityDtoMapper;
import ru.practicum.shareit.user.repository.DBUserRepository;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DBItemService implements ItemService {

    @Autowired
    private final DBItemRepository storage;

    private final DBUserRepository userStorage;

    @Override
    public ItemDto createItem(long userId, ItemDto itemDto) {
        try {
            if (userStorage.findById(userId).isPresent()) {
                Item item = ItemEntityDtoMapper.getItemFromItemDto(itemDto);
                item.setOwner(userId);
                return ItemEntityDtoMapper.getItemDtoFromItem(storage.save(item));
            } else {
                throw new NotFoundException(String.format("Не нашли пользователя с ID: %d", userId));
            }
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
    public ItemDto getItemById(long userId, long itemId) {
        try {
            return ItemEntityDtoMapper.getItemDtoFromItem(storage.findById(itemId)
                    .orElseThrow(() -> new NotFoundException(String.format("Не нашли вещи с ID: %d", itemId))));
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
    public Set<ItemDto> getItemSet(long userId) {
        List<Item> itemList = storage.findByOwner(userId);
        Set<ItemDto> itemSet = new LinkedHashSet<>();
        for (Item item : itemList) {
            itemSet.add(ItemEntityDtoMapper.getItemDtoFromItem(item));
        }
        return itemSet.stream()
                .sorted((o1, o2) -> (int) (o1.getId() - o2.getId()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Set<ItemDto> getItemsByText(long userId, String text) {
        if (text.isEmpty()) return Set.of();
        List<Item> itemList = storage.findByNameContainingOrDescriptionContainingIgnoreCase(text, text);
        Set<ItemDto> itemDtoSet = new HashSet<>();
        for (Item item : itemList) {
            itemDtoSet.add(ItemEntityDtoMapper.getItemDtoFromItem(item));
        }

        return itemDtoSet.stream()
                .sorted((o1, o2) -> (int) (o1.getId() - o2.getId()))
                .filter(o -> o.getAvailable().equals(true))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
