package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.dto.ItemEntityDtoMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final UserRepository userRepository;

    private final ItemRepository repository;

    @Autowired
    public ItemServiceImpl(UserRepository userRepository, ItemRepository repository) {
        this.userRepository = userRepository;
        this.repository = repository;
    }

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

    @Override
    public List<ItemDto> getItemSet(long userId, Long from, Long size) {
        try {
            userRepository.getUserById(userId);
            List<Item> items = repository.getItemList(userId);
            List<ItemDto> itemsDto = new ArrayList<>();
            for (Item item : items) {
                itemsDto.add(ItemEntityDtoMapper.getItemDtoFromItem(item));
            }
            return itemsDto.stream()
                    .sorted((o1, o2) -> (int) (o1.getId() - o2.getId()))
                    .collect(Collectors.toCollection(LinkedList::new));
        } catch (NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public List<ItemDto> getItemsByText(long userId, String text, Long from, Long size) {
        try {
            userRepository.getUserById(userId);
            List<Item> items = repository.getItemByText(text);
            List<ItemDto> itemDtoList = new LinkedList<>();
            for (Item item : items) {
                itemDtoList.add(ItemEntityDtoMapper.getItemDtoFromItem(item));
            }
            return itemDtoList.stream()
                    .sorted((o1, o2) -> (int) (o1.getId() - o2.getId()))
                    .collect(Collectors.toCollection(LinkedList::new));
        } catch (NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }
}
