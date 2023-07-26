package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemEntityDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final ItemRepository repository;

    public ItemDto createItem(long userId, ItemDto itemDto) {
        try {
            userRepository.getUserById(userId);
            Item item = ItemEntityDtoMapper.getItemFromItemDto(itemDto);
            if (item.getName() == null ||
                item.getName().isBlank() ||
                item.getName().isEmpty()) throw new ValidationException("Имя вещи должно быть заполнено");
            if (item.getDescription() == null) throw new ValidationException("Описание вещи должно быть заполнено");
            if (item.getAvailable() == null) throw new ValidationException("Статус вещи должен быть заполнен");
            Item createdItem = repository.addItem(userId, item);
            return ItemEntityDtoMapper.getItemDtoFromItem(createdItem);
        } catch (ValidationException | NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        try {
            userRepository.getUserById(userId);
            Item item = ItemEntityDtoMapper.getItemFromItemDto(itemDto);
            return ItemEntityDtoMapper.getItemDtoFromItem(repository.updateItem(userId, itemId, item));
        } catch (NotFoundException | ValidationException exception) {
            throw new RuntimeException(exception);
        }
    }

    public ItemDto getItemById(long userId, long itemId) {
        try {
            userRepository.getUserById(userId);
            return ItemEntityDtoMapper.getItemDtoFromItem(repository.getItemById(userId, itemId));
        } catch (NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void removeItemById(long userId, long itemId) {
        try {
            userRepository.getUserById(userId);
            repository.removeItem(userId, itemId);
        } catch (NotFoundException | ValidationException exception) {
            throw new RuntimeException(exception);
        }
    }

    public Set<ItemDto> getItemSet(long userId) {
        try {
            userRepository.getUserById(userId);
            List<Item> items = repository.getItemList(userId);
            List<ItemDto> itemsDto = new ArrayList<>();
            for (Item item : items) {
                itemsDto.add(ItemEntityDtoMapper.getItemDtoFromItem(item));
            }
            return itemsDto.stream()
                    .sorted((o1, o2) -> (int) (o1.getId() - o2.getId()))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        } catch (NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    public Set<ItemDto> getItemsByText(long userId, String text) {
        try {
            userRepository.getUserById(userId);
            List<Item> items = repository.getItemByText(text);
            Set<ItemDto> itemDtoSet = new TreeSet<>((o1, o2) -> (int) (o1.getId() - o2.getId()));
            for (Item item : items) {
                itemDtoSet.add(ItemEntityDtoMapper.getItemDtoFromItem(item));
            }
            return itemDtoSet;
        } catch (NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }
}
