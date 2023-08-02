package ru.practicum.shareit.item;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserEntityDtoMapper;
import ru.practicum.shareit.user.repository.DBUserRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.unit.TestUnit.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource("classpath:application-test.properties")
@Sql(scripts = "classpath:schema.sql")
public class ItemTest {

    @Autowired
    private ItemController controller;

    @Autowired
    private DBUserRepository userRepository;
    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldAddItemOrThrow() {
        UserDto user = getUser();
        userRepository.save(UserEntityDtoMapper.getEntityFromDto(user));

        ItemDto item = getItem();
        item.setId(1);
        controller.addItem(1, item);

        assertEquals(item, controller.getItemById(1, 1));
        item.setName("");
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(item);
        System.out.println(violations);
        assertFalse(violations.isEmpty());

        item.setAvailable(null);
        item.setDescription("");

        violations = validator.validate(item);
        System.out.println(Arrays.toString(violations.toArray()));
        assertEquals(3, violations.size());
    }

    @Test
    public void shouldGetItemByIdOrThrow() {
        UserDto user = getUser();
        user.setId(1);
        userRepository.save(UserEntityDtoMapper.getEntityFromDto(user));
        UserDto user2 = getUser();
        user2.setId(2);
        user2.setEmail("otheEmail@user.com");
        userRepository.save(UserEntityDtoMapper.getEntityFromDto(user2));

        ItemDto item = getItem();
        item.setId(1);
        controller.addItem(1, item);

        assertEquals(item, controller.getItemById(1, 1));
        assertThrows(RuntimeException.class, () -> controller.getItemById(1, 9999));
        assertThrows(RuntimeException.class, () -> controller.getItemById(9999, 1));
        System.out.println(controller.getItemById(2, 1));
    }

    @Test
    public void shouldGetAllItemsByUserId() {
        UserDto user1 = getUser();
        user1.setId(1);
        UserDto user2 = getUser();
        user2.setId(2);
        user2.setEmail("updateUser@user.com");

        userRepository.save(UserEntityDtoMapper.getEntityFromDto(user1));
        userRepository.save(UserEntityDtoMapper.getEntityFromDto(user2));

        ItemDto item1 = getItem();
        item1.setId(1);
        ItemDto item2 = getItem();
        item2.setId(2);

        controller.addItem(1, item1);
        controller.addItem(2, item2);

        assertEquals(Set.of(item1), controller.getItemSet(1));
        assertEquals(Set.of(item2), controller.getItemSet(2));
        assertThrows(RuntimeException.class, () -> controller.getItemSet(9999));
    }

    @Test
    public void shouldGetAvailableItemsBySearch() {
        UserDto user1 = getUser();
        user1.setId(1);
        UserDto user2 = getUser();
        user2.setId(2);
        user2.setEmail("otherUser@user.com");

        userRepository.save(UserEntityDtoMapper.getEntityFromDto(user1));
        userRepository.save(UserEntityDtoMapper.getEntityFromDto(user2));

        ItemDto item1 = getItem();
        item1.setId(1);
        ItemDto item2 = getItem();
        item2.setId(2);
        ItemDto item3 = getItem();
        item3.setId(3);

        controller.addItem(1, item1);
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
    public void shouldRemoveItemOrGetTrows() {
        UserDto user = getUser();
        user.setId(1);
        userRepository.save(UserEntityDtoMapper.getEntityFromDto(user));

        ItemDto item = getItem();
        item.setId(1);
        controller.addItem(1, item);

        assertEquals(item, controller.getItemById(1, 1));

        System.out.println(controller.getItemById(1, 1));

        controller.removeItem(1, 1);

        assertThrows(RuntimeException.class, () -> controller.getItemById(1, 1));
    }
}
