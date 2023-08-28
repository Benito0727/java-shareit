package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.unit.TestUnits.getUserDto;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserInMemServiceImplTest {

    @Autowired
    private UserServiceImpl service;


    @Test
    void addUser() {
        service.addUser(getUserDto());

        assertEquals(1L, service.getUserById(1).getId());
        assertThrows(RuntimeException.class, () -> service.addUser(getUserDto()));
    }

    @Test
    void updateUser() {
        service.addUser(getUserDto());
        UserDto userDto = getUserDto();
        userDto.setName("update");
        userDto.setEmail("other@mail.ru");
        service.updateUser(1, userDto);
        UserDto userDto1 = getUserDto();
        userDto1.setEmail("user@user.ru");
        service.addUser(userDto1);

        assertEquals(1, service.getUserById(1).getId());
        assertEquals("update", service.getUserById(1).getName());

        userDto.setEmail("user@user.ru");

        assertThrows(RuntimeException.class, () -> service.updateUser(1, userDto));
        assertThrows(RuntimeException.class, () -> service.updateUser(999, getUserDto()));
    }

    @Test
    void getUserById() {
        service.addUser(getUserDto());

        assertEquals(1, service.getUserById(1).getId());
        assertEquals("user", service.getUserById(1).getName());
        assertEquals("user@email.com", service.getUserById(1).getEmail());
        assertThrows(RuntimeException.class, () -> service.getUserById(999));
    }

    @Test
    void removeUser() {
        service.addUser(getUserDto());
        service.removeUser(1);

        assertThrows(RuntimeException.class, () -> service.getUserById(1));
        assertThrows(RuntimeException.class, () -> service.removeUser(9999));
    }

    @Test
    void getAllUsers() {
        List<UserDto> emptyList = service.getAllUsers();

        assertTrue(emptyList.isEmpty());

        UserDto user1 = getUserDto();
        service.addUser(user1);
        UserDto user2 = getUserDto();
        user2.setEmail("user@user.ru");
        service.addUser(user2);
        UserDto user3 = getUserDto();
        user3.setEmail("other@mail.ru");
        service.addUser(user3);

        List<UserDto> users = service.getAllUsers();

        assertEquals(3, users.size());
        assertEquals("user@email.com", users.get(0).getEmail());
        assertEquals("user@user.ru", users.get(1).getEmail());
        assertEquals("other@mail.ru", users.get(2).getEmail());
    }
}