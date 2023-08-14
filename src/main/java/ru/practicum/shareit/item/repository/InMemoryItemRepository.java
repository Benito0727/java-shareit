package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.InMemoryUserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class InMemoryItemRepository implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();

    private final InMemoryUserRepository userStorage;

    public InMemoryItemRepository(InMemoryUserRepository userStorage) {
        this.userStorage = userStorage;
    }

    private long currentItemId = 0;

    @Override
    public Item addItem(long userId, Item item) {
            item.setId(++currentItemId);
            log.info("Вещи под названием {}, присвоен ID: {}", item.getName(), item.getId());
        User user;
        try {
           user = userStorage.getUserById(userId);
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
        item.setOwner(user);
            log.info("Вещь с ID: {} принадлежит пользователю {}", item.getId(), userId);
            items.put(item.getId(), item);
            return item;
    }

    @Override
    public Item getItemById(long userId, long itemId) throws NotFoundException {
        Item item = items.get(itemId);
        if (item != null) {
            log.info("Вернули вещь {}", itemId);
            return item;
        } else {
            throw new NotFoundException("Неверный ID  предмета");
        }
    }

    @Override
    public void removeItem(long userId, long itemId) throws NotFoundException, ValidationException {
        Item item = items.get(itemId);
        if (item != null) {
            if (item.getOwner().getId() == userId) {
                items.remove(itemId);
                log.info("Владелец удалил вещь с ID: {}", itemId);
                return;
            } else {
                log.warn("Только владелец может удалить вещь");
                throw new ValidationException("Только владелец может удалить вещь");
            }
        }
        throw new NotFoundException(String.format("Не нашли вещь с ID: %s", itemId));
    }

    @Override
    public Item updateItem(long userId, long itemId, Item item) throws NotFoundException {
        Item itemToUpdate = items.get(itemId);
        if (itemToUpdate != null) {
            if (itemToUpdate.getOwner().getId() == userId) {

                if (item.getName() != null) itemToUpdate.setName(item.getName());
                if (item.getDescription() != null) itemToUpdate.setDescription(item.getDescription());
                if (item.getAvailable() != null) itemToUpdate.setAvailable(item.getAvailable());

                items.put(itemToUpdate.getId(), itemToUpdate);
                log.info("Обновили вещь с ID: {}", itemId);
                return items.get(itemId);
            } else {
                log.warn("Только владелец может обновить информацию о вещи");
                throw new NotFoundException("Только владелец может обновлять информацию о вещи");
            }
        } else {
            throw new NotFoundException(String.format("Не нашли вещь с ID: %d", itemId));
        }
    }

    @Override
    public List<Item> getItemList(long userId) {
        return items.values().stream()
                .filter(i -> i.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    public List<Item> getItemByText(String text) {
        if (text == null || text.isEmpty() || text.isBlank()) return List.of();
        List<Item> itemList = new ArrayList<>(items.values());
        List<Item> foundItems = new ArrayList<>();

        for (Item item : itemList) {
            if (item.getAvailable()) {
                if (substringSearch(item.getName().toLowerCase(), text.toLowerCase()) == 1) foundItems.add(item);
                if (substringSearch(item.getDescription().toLowerCase(), text.toLowerCase()) == 1) foundItems.add(item);
            }
        }
        return foundItems;
    }

    private int substringSearch(String text, String searchingText) {
        int i = 0;
        for (;;) {
            if (i > text.length() - searchingText.length()) {
                return -1;
            }
            if (text.startsWith(searchingText, i)) {
                return 1;
            }
            i++;
        }
    }
}
