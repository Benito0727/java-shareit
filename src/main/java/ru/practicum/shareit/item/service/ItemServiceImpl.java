package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
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

    public Item createItem(long userId, Item item) {
        try {
            if (userRepository.getUserById(userId) != null) {
                if (item.getName() == null ||
                        item.getName().isBlank() ||
                        item.getName().isEmpty()) throw new ValidationException("Имя вещи должно быть заполнено");
                if (item.getDescription() == null) throw new ValidationException("Описание вещи должно быть заполнено");
                if (item.getAvailable() == null) throw new ValidationException("Статус вещи должен быть заполнен");
                return repository.addItem(userId, item);
            } else {
                throw new NotFoundException(String.format("Не нашли пользователя с ID: %d", userId));
            }
        } catch (NotFoundException | ValidationException exception) {
            throw new RuntimeException(exception);
        }
    }

    public Item updateItem(long userId, long itemId, Item item) {
        try {
            if (userRepository.getUserById(userId) != null) {
                return repository.updateItem(userId, itemId, item);
            } else {
                throw new NotFoundException(String.format("Не нашли пользователя с ID: %d", userId));
            }
        } catch (NotFoundException | ValidationException exception) {
            throw new RuntimeException(exception);
        }
    }

    public Item getItemById(long userId, long itemId) {
        try {
            if (userRepository.getUserById(userId) != null) {
                return repository.getItemById(userId, itemId);
            } else {
                throw new NotFoundException(String.format("Не нашли пользователя с ID: %d", userId));
            }
        } catch (NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void removeItemById(long userId, long itemId) {
        try {
            if (userRepository.getUserById(userId) != null) {
                repository.removeItem(userId, itemId);
            } else {
                throw new NotFoundException(String.format("Не нашли пользователя с ID: %d", userId));
            }
        } catch (NotFoundException | ValidationException exception) {
            throw new RuntimeException(exception);
        }
    }

    public Set<Item> getItemSet(long userId) {
        try {
            if (userRepository.getUserById(userId) != null) {
                return repository.getItemList(userId).stream()
                        .sorted((o1, o2) -> (int) (o1.getId() - o2.getId()))
                        .collect(Collectors.toCollection(LinkedHashSet::new));
            } else {
                throw new NotFoundException(String.format("Не нашли пользователя с ID: %d", userId));
            }
        } catch (NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    public Set<Item> getItemsByText(long userId, String text) {
        try {
            if (userRepository.getUserById(userId) != null) {
                return repository.getItemByText(text);
            } else {
                throw new NotFoundException(String.format("Не нашли пользователя с ID: %d", userId));
            }
        } catch (NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }
}
