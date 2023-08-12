package ru.practicum.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.DBUserRepository;
import ru.practicum.shareit.user.service.DBUserService;
import ru.practicum.unit.TestUnit;


import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserTest {

    private UserController controller;

    @Autowired
    private DBUserRepository repository;

    static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    public void setController() {
        DBUserService service = new DBUserService(repository);
        controller = new UserController(service);
    }

    @Test
    public void shouldAddNewUserOrGetThrows() {
        UserDto user = TestUnit.getUser();
        user.setId(1);
        Set<ConstraintViolation<UserDto>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
        controller.addUser(user);
        assertEquals(user, controller.getUserById(1));

        UserDto userWithInvalidEmail = TestUnit.getUser();
        userWithInvalidEmail.setEmail("user.user");

        violations = validator.validate(userWithInvalidEmail);

        assertFalse(violations.isEmpty());

        UserDto userWithEmptyName = TestUnit.getUser();
        userWithEmptyName.setName("");

        violations = validator.validate(userWithEmptyName);

        assertFalse(violations.isEmpty());
     }

    @Test
    public void shouldGetUserOrThrow() {
        UserDto user = TestUnit.getUser();
        user.setId(1);
        controller.addUser(user);

        assertEquals(user, controller.getUserById(1));
        assertThrows(RuntimeException.class, () -> controller.getUserById(9999));
     }

    @Test
    public void shouldGetUsersSet() {
        UserDto user1 = TestUnit.getUser();
        user1.setId(1);
        UserDto user2 = TestUnit.getUser();
        user2.setId(2);
        user2.setEmail("otherUser@user.com");

        assertTrue(controller.getAllUsers().isEmpty());

        controller.addUser(user1);
        controller.addUser(user2);

        assertFalse(controller.getAllUsers().isEmpty());
        assertTrue(controller.getAllUsers().contains(user1));
        assertTrue(controller.getAllUsers().contains(user2));
    }

    @Test
    public void shouldUpdateUser() {
        UserDto user = TestUnit.getUser();
        user.setId(1);
        controller.addUser(user);

        assertEquals(user, controller.getUserById(1));

        UserDto userToUpdate = new UserDto();
        userToUpdate.setName("updateName");
        userToUpdate.setEmail("otherEmail@email.com");
        controller.updateUser(1, userToUpdate);

        assertEquals("updateName", controller.getUserById(1).getName());
        assertEquals("otherEmail@email.com", controller.getUserById(1).getEmail());
    }

    @Test
    public void shouldRemoveUserById() {
        UserDto user = TestUnit.getUser();
        user.setId(1);
        controller.addUser(user);

        assertEquals(controller.getUserById(1), user);
        assertThrows(RuntimeException.class, () -> controller.removeUser(9999));

        controller.removeUser(1);

        assertThrows(RuntimeException.class, () -> controller.getUserById(1));
    }
}
