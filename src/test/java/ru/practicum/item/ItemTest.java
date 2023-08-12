package ru.practicum.item;


import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserEntityDtoMapper;
import ru.practicum.shareit.user.repository.DBUserRepository;
import ru.practicum.unit.TestUnit;


import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
        UserDto user = TestUnit.getUser();
        userRepository.save(UserEntityDtoMapper.getEntityFromDto(user));

        ItemDto item = TestUnit.getItem();
        item.setId(1L);
        item.setOwner(1L);
        item.setComments(Set.of());
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
        UserDto user = TestUnit.getUser();
        user.setId(1L);
        userRepository.save(UserEntityDtoMapper.getEntityFromDto(user));
        UserDto user2 = TestUnit.getUser();
        user2.setId(2);
        user2.setEmail("otheEmail@user.com");
        userRepository.save(UserEntityDtoMapper.getEntityFromDto(user2));

        ItemDto item = TestUnit.getItem();
        item.setId(1L);
        item.setOwner(1L);
        item.setComments(Set.of());
        controller.addItem(1, item);

        assertEquals(item, controller.getItemById(1, 1));
        assertThrows(RuntimeException.class, () -> controller.getItemById(1, 9999));
        System.out.println(controller.getItemById(2, 1));
    }

    @Test
    public void shouldGetAllItemsByUserId() {
        UserDto user1 = TestUnit.getUser();
        user1.setId(1);
        UserDto user2 = TestUnit.getUser();
        user2.setId(2);
        user2.setEmail("updateUser@user.com");

        userRepository.save(UserEntityDtoMapper.getEntityFromDto(user1));
        userRepository.save(UserEntityDtoMapper.getEntityFromDto(user2));

        ItemDto item1 = TestUnit.getItem();
        item1.setId(1L);
        item1.setOwner(1L);
        item1.setComments(Set.of());
        ItemDto item2 = TestUnit.getItem();
        item2.setId(2L);
        item2.setOwner(2L);
        item2.setComments(Set.of());

        controller.addItem(1, item1);
        controller.addItem(2, item2);

        assertEquals(Set.of(item1), controller.getItemSet(1));
        assertEquals(Set.of(item2), controller.getItemSet(2));
    }

    @Test
    public void shouldRemoveItemOrGetTrows() {
        UserDto user = TestUnit.getUser();
        user.setId(1);
        userRepository.save(UserEntityDtoMapper.getEntityFromDto(user));

        ItemDto item = TestUnit.getItem();
        item.setId(1L);
        item.setOwner(1L);
        item.setComments(Set.of());
        controller.addItem(1, item);

        assertEquals(item, controller.getItemById(1, 1));

        System.out.println(controller.getItemById(1, 1));

        controller.removeItem(1, 1);

        assertThrows(RuntimeException.class, () -> controller.getItemById(1, 1));
    }
}
