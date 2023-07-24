package ru.practicum.shareit.item;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.InMemoryItemRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.InMemoryUserRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.unit.TestUnit.*;

@SpringBootTest
public class ItemTest {

    private ItemController controller;

    private UserRepository userRepository;
    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    public void setController() {
        ItemRepository repository = new InMemoryItemRepository();
        userRepository = new InMemoryUserRepository();
        ItemServiceImpl service = new ItemServiceImpl(userRepository, repository);
        controller = new ItemController(service);
    }

    @Test
    public void shouldAddItemOrThrow() throws ValidationException, ConflictException {
        User user = getUser();
        userRepository.addUser(user);

        Item item = getItem();
        controller.addItem(1, item);

        assertEquals(item, controller.getItemById(1, 1));
        item.setName("");
        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        System.out.println(violations);
        assertFalse(violations.isEmpty());

        item.setAvailable(null);
        item.setDescription("");

        violations = validator.validate(item);
        System.out.println(Arrays.toString(violations.toArray()));
        assertEquals(3, violations.size());
    }

    @Test
    public void shouldGetItemByIdOrThrow() throws ValidationException, ConflictException {
        User user = getUser();
        userRepository.addUser(user);
        User user2 = getUser();
        user2.setEmail("otheEmail@user.com");
        userRepository.addUser(user2);

        Item item = getItem();
        controller.addItem(1, item);

        assertEquals(item, controller.getItemById(1, 1));
        assertThrows(RuntimeException.class, () -> controller.getItemById(1, 9999));
        assertThrows(RuntimeException.class, () -> controller.getItemById(9999, 1));
        System.out.println(controller.getItemById(2, 1));
    }

    @Test
    public void shouldGetAllItemsByUserId() throws ValidationException, ConflictException {
        User user1 = getUser();
        User user2 = getUser();
        user2.setEmail("updateUser@user.com");

        userRepository.addUser(user1);
        userRepository.addUser(user2);

        Item item1 = getItem();
        Item item2 = getItem();

        controller.addItem(1, item1);
        controller.addItem(2, item2);

        assertEquals(Set.of(item1), controller.getItemSet(1));
        assertEquals(Set.of(item2), controller.getItemSet(2));
        assertThrows(RuntimeException.class, () -> controller.getItemSet(9999));
    }

    @Test
    public void shouldGetAvailableItemsBySearch() throws ValidationException, ConflictException {
        User user1 = getUser();
        User user2 = getUser();
        user2.setEmail("otherUser@user.com");

        userRepository.addUser(user1);
        userRepository.addUser(user2);

        Item item1 = getItem();
        Item item2 = getItem();
        Item item3 = getItem();

        controller.addItem(1 , item1);
        controller.addItem(1, item2);
        controller.addItem(2, item3);

        assertEquals(3, controller.getSearch(1, "item").size());
        item2.setAvailable(false);
        controller.updateItem(1, 2, item2);

        assertEquals(2, controller.getSearch(1, "item").size());
        assertTrue(controller.getSearch(1, "item").contains(item1));
        assertTrue(controller.getSearch(1, "item").contains(item3));

        assertTrue(controller.getSearch(1, "").isEmpty());
        assertTrue(controller.getSearch(1, "ietm").isEmpty());
    }

    @Test
    public void shouldRemoveItemOrGetTrows() throws ValidationException, ConflictException {
        User user = getUser();
        userRepository.addUser(user);

        Item item = getItem();
        controller.addItem(1, item);

        assertEquals(item, controller.getItemById(1, 1));

        System.out.println(controller.getItemById(1, 1));

        controller.removeItem(1, 1);

        assertThrows(RuntimeException.class, () -> controller.getItemById(1, 1));
    }
}
