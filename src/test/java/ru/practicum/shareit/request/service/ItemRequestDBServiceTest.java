package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.service.DBItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.DBUserService;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.unit.TestUnits.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestDBServiceTest {

    @Autowired
    private ItemRequestDBService itemRequestService;
    @Autowired
    private DBUserService userService;
    @Autowired
    private DBItemService itemService;


    @Test
    void getRequestById() {
        userService.addUser(getUserDto());
        itemService.createItem(1, getItemDto());
        itemRequestService.addRequest(1L,getItemRequestDto());

        assertNotNull(itemRequestService.getRequestById(1L, 1L));
        assertThrows(RuntimeException.class, () -> itemRequestService.getRequestById(999L, 1L));
        assertThrows(RuntimeException.class, () -> itemRequestService.getRequestById(1L, 999L));
    }

    @Test
    void getUserRequests() {
        userService.addUser(getUserDto());
        itemService.createItem(1, getItemDto());
        itemRequestService.addRequest(1L, getItemRequestDto());

        assertThrows(RuntimeException.class, () -> itemRequestService.getUserRequests(999L));
        assertNotNull(itemRequestService.getUserRequests(1L));
    }

    @Test
    void getOtherUsersRequests() {
        UserDto user1 = getUserDto();
        UserDto user2 = getUserDto();
        user2.setEmail("other@mail.ru");
        userService.addUser(user1);
        userService.addUser(user2);

        itemService.createItem(1, getItemDto());

        itemRequestService.addRequest(1L, getItemRequestDto());
        assertThrows(RuntimeException.class, () -> itemRequestService.getOtherUsersRequests(999L, 0L, 10L));
        assertTrue(itemRequestService.getOtherUsersRequests(1L, 0L, 10L).isEmpty());
        assertFalse(itemRequestService.getOtherUsersRequests(2L, 0L, 10L).isEmpty());
    }

    @Test
    void addRequest() {
        userService.addUser(getUserDto());
        itemService.createItem(1, getItemDto());
        itemRequestService.addRequest(1L, getItemRequestDto());

        assertEquals(1, itemRequestService.getRequestById(1L, 1L).getId());
    }
}