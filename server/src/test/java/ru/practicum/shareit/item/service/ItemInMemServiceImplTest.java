package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.InMemoryItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.InMemoryUserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.unit.TestUnits.getItemDto;
import static ru.practicum.shareit.unit.TestUnits.getUserEntity;

@SpringBootTest
class ItemInMemServiceImplTest {

    private ItemServiceImpl service;

    private InMemoryItemRepository itemRepository;
    private InMemoryUserRepository userRepository;

    @BeforeEach
    void setUp() {
        this.userRepository = new InMemoryUserRepository();
        this.itemRepository = new InMemoryItemRepository(userRepository);
        this.service = new ItemServiceImpl(userRepository, itemRepository);
    }

    @Test
    void createItem() throws ConflictException, NotFoundException {
        userRepository.addUser(getUserEntity());
        service.createItem(1, getItemDto());

        ItemDto itemDto1 = getItemDto();
        itemDto1.setName(null);
        ItemDto itemDto2 = getItemDto();
        itemDto2.setDescription(null);
        ItemDto itemDto3 = getItemDto();
        itemDto3.setAvailable(null);

        assertNotNull(itemRepository.getItemById(1, 1));
        assertThrows(RuntimeException.class, () -> service.createItem(999, getItemDto()));
        assertThrows(RuntimeException.class, () -> service.createItem(1, itemDto1));
        assertThrows(RuntimeException.class, () -> service.createItem(1, itemDto2));
        assertThrows(RuntimeException.class, () -> service.createItem(1, itemDto3));
    }

    @Test
    void updateItem() throws ConflictException, NotFoundException {
        userRepository.addUser(getUserEntity());
        User user = getUserEntity();
        user.setEmail("other@mail.com");
        userRepository.addUser(user);
        service.createItem(1, getItemDto());
        ItemDto itemDto1 = getItemDto();
        itemDto1.setName(null);
        ItemDto itemDto2 = getItemDto();
        itemDto2.setDescription(null);
        ItemDto itemDto3 = getItemDto();
        itemDto3.setAvailable(null);
        ItemDto itemToUpdate = getItemDto();
        itemToUpdate.setName("update");
        itemToUpdate.setDescription("update");
        itemToUpdate.setAvailable(false);


        service.updateItem(1, 1, itemToUpdate);
        assertThrows(RuntimeException.class, () -> service.updateItem(2, 1, itemToUpdate));
        assertEquals("update", itemRepository.getItemById(1, 1).getName());
        assertEquals("update", itemRepository.getItemById(1, 1).getDescription());
        assertFalse(itemRepository.getItemById(1, 1).getAvailable());

        assertThrows(RuntimeException.class, () -> service.updateItem(999, 1, itemToUpdate));
        assertThrows(RuntimeException.class, () -> service.updateItem(1, 999, itemToUpdate));
    }

    @Test
    void getItemById() throws ConflictException {
        userRepository.addUser(getUserEntity());
        service.createItem(1, getItemDto());

        assertEquals("item", service.getItemById(1, 1).getName());
        assertEquals("item description", service.getItemById(1, 1).getDescription());

        assertThrows(RuntimeException.class, () -> service.getItemById(999, 1));
        assertThrows(RuntimeException.class, () -> service.getItemById(1, 999));

    }

    @Test
    void removeItemById() throws ConflictException {
        userRepository.addUser(getUserEntity());
        User user = getUserEntity();
        user.setEmail("other@mail.com");
        userRepository.addUser(user);
        service.createItem(1, getItemDto());

        assertThrows(RuntimeException.class, () -> service.removeItemById(9999, 1));
        assertThrows(RuntimeException.class, () -> service.removeItemById(1, 9999));
        assertThrows(RuntimeException.class, () -> service.removeItemById(2, 1));
        service.removeItemById(1, 1);

        assertThrows(RuntimeException.class, () -> service.getItemById(1, 1));
    }

    @Test
    void getItemSet() throws ConflictException {
        userRepository.addUser(getUserEntity());

        assertTrue(service.getItems(1, 0, 10).isEmpty());

        service.createItem(1, getItemDto());
        service.createItem(1, getItemDto());

        assertFalse(service.getItems(1, 0, 10).isEmpty());
        assertThrows(RuntimeException.class, () -> service.getItems(999, 0, 10));

    }

    @Test
    void getItemsByText() throws ConflictException {
        userRepository.addUser(getUserEntity());

        service.createItem(1, getItemDto());

        List<ItemDto> items = service.getItemsByText(1, "item", 1, 10);
        List<ItemDto> emptyList = service.getItemsByText(1, "other", 1, 10);

        assertThrows(RuntimeException.class, () -> service.getItemsByText(999, "text", 0, 10));
        assertTrue(emptyList.isEmpty());
        assertFalse(items.isEmpty());
        assertEquals("item", items.get(0).getName());
    }
}