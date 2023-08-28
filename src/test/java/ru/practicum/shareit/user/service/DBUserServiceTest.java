package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ru.practicum.shareit.unit.TestUnits.getUserDto;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DBUserServiceTest {

    private final DBUserService service;

    @Test
    void addUser() {
        UserDto newUser = service.addUser(getUserDto());

        assertThat(newUser.getId(), equalTo(1L));
        assertThat(newUser.getName(), equalTo("user"));
        assertThat(newUser.getEmail(), equalTo("user@email.com"));
    }

    @Test
    void updateUserOrThrowsException() {
        UserDto newUser = getUserDto();
        service.addUser(newUser);

        UserDto user = new UserDto();
        user.setName("update");

        service.updateUser(1, user);

        UserDto updateUser = service.getUserById(1);

        assertThat(updateUser.getId(), equalTo(1L));
        assertThat(updateUser.getName(), equalTo("update"));

        updateUser.setEmail("update@mail.com");
        service.updateUser(1, updateUser);

        assertThat(updateUser.getEmail(), equalTo("update@mail.com"));

        assertThrows(RuntimeException.class, () -> service.updateUser(999, updateUser));

    }

    @Test
    void getUserByIdOrThrowsException() {
        UserDto newUser = getUserDto();
        service.addUser(newUser);

        assertThat(service.getUserById(1).getName(), equalTo("user"));
        assertThat(service.getUserById(1).getEmail(), equalTo("user@email.com"));

        assertThrows(RuntimeException.class, () -> service.getUserById(999));
    }

    @Test
    void removeUser() {
        UserDto newUser = getUserDto();

        service.addUser(newUser);

        assertThat(service.getUserById(1).getName(), equalTo("user"));

        service.removeUser(1);

        assertThrows(RuntimeException.class, () -> service.getUserById(1));
    }

    @Test
    void getAllUsers() {
        UserDto newUser1 = getUserDto();
        UserDto newUser2 = getUserDto();
        newUser2.setName("user2");
        newUser2.setEmail("user2@email.com");
        service.addUser(newUser1);
        service.addUser(newUser2);


        assertThat(service.getAllUsers().get(0).getName(), equalTo("user"));
        assertThat(service.getAllUsers().get(1).getName(), equalTo("user2"));

        assertThat(service.getAllUsers(), hasSize(2));
    }
}