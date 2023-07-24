package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.InMemoryUserRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.unit.TestUnit.*;

@SpringBootTest
public class UserTest {

    private UserController controller;

    static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    public void setController() {
        UserRepository repository = new InMemoryUserRepository();
        UserServiceImpl service = new UserServiceImpl(repository);
        controller = new UserController(service);
    }

    @Test
    public void shouldAddNewUserOrGetThrows() {
        User user = getUser();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
        controller.addUser(user);
        assertEquals(user, controller.getUserById(1));

        User userWithInvalidEmail = getUser();
        userWithInvalidEmail.setEmail("user.user");

        violations = validator.validate(userWithInvalidEmail);

        assertFalse(violations.isEmpty());

        User userWithEmptyName = getUser();
        userWithEmptyName.setName("");

        violations = validator.validate(userWithEmptyName);

        assertFalse(violations.isEmpty());

        assertThrows(RuntimeException.class, () -> controller.addUser(user));
     }

    @Test
    public void shouldGetUserOrThrow() {
        User user = getUser();
        controller.addUser(user);

        assertEquals(user, controller.getUserById(1));
        assertThrows(RuntimeException.class, () -> controller.getUserById(9999));
     }

    @Test
    public void shouldGetUsersSet() {
        User user1 = getUser();
        User user2 = getUser();
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
        User user = getUser();
        controller.addUser(user);

        assertEquals(user, controller.getUserById(1));

        User userToUpdate = new User();
        userToUpdate.setName("updateName");
        userToUpdate.setEmail("otherEmail@email.com");
        controller.updateUser(1, userToUpdate);

        assertEquals("updateName", controller.getUserById(1).getName());
        assertEquals("otherEmail@email.com", controller.getUserById(1).getEmail());
    }

    @Test
    public void shouldRemoveUserById() {
        User user = getUser();
        controller.addUser(user);

        assertEquals(controller.getUserById(1), user);
        assertThrows(RuntimeException.class, () -> controller.removeUser(9999));

        controller.removeUser(1);

        assertThrows(RuntimeException.class, () -> controller.getUserById(1));
    }
}
