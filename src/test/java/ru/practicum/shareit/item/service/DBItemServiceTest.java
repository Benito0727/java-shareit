package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.service.DBUserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.unit.TestUnits.getItemDto;
import static ru.practicum.shareit.unit.TestUnits.getUserDto;

@Transactional
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DBItemServiceTest {

    private final DBItemService itemService;
    private final DBUserService userService;


    @Test
    void createItemOrThrowsException() {
        assertThrows(RuntimeException.class,() -> itemService.createItem(1, getItemDto()));

        System.out.println(userService.addUser(getUserDto()));

        ItemDto itemDto = itemService.createItem(1, getItemDto());

        assertEquals(1, itemDto.getId());
        assertEquals("item", itemDto.getName());
        assertEquals("item description", itemDto.getDescription());
        assertEquals(1, itemDto.getOwner());
    }

    @Test
    void updateItemOrThrowsException() {
        assertThrows(RuntimeException.class, () -> itemService.updateItem(1, 1, getItemDto()));

        userService.addUser(getUserDto());

        ItemDto itemDto = itemService.createItem(1, getItemDto());

        assertEquals("item", itemDto.getName());

        itemDto.setName("update");

        itemDto = itemService.updateItem(1, 1, itemDto);

        assertEquals("update", itemDto.getName());
    }

    @Test
    void getItemByIdOrThrowsException() {
        assertThrows(RuntimeException.class, ()-> itemService.getItemById(1, 1));

        userService.addUser(getUserDto());

        assertThrows(RuntimeException.class, () -> itemService.getItemById(1, 1));

        itemService.createItem(1, getItemDto());

        assertEquals(1, itemService.getItemById(1, 1).getId());
        assertEquals(1, itemService.getItemById(1, 1).getOwner());
        assertEquals("item", itemService.getItemById(1, 1).getName());
    }

    @Test
    void removeItemByIdOrThrowsException() {
        assertThrows(RuntimeException.class, () -> itemService.removeItemById(1, 1));
        userService.addUser(getUserDto());
        assertThrows(RuntimeException.class, () -> itemService.removeItemById(1, 1));
        itemService.createItem(1, getItemDto());

        assertEquals("item", itemService.getItemById(1, 1).getName());

        assertThrows(RuntimeException.class, () -> itemService.removeItemById(2, 1));

        itemService.removeItemById(1, 1);
        assertThrows(RuntimeException.class, () -> itemService.getItemById(1, 1));

    }

    @Test
    void getItemSet() {
        userService.addUser(getUserDto());
        ItemDto item1 = getItemDto();
        item1.setName("item1");
        ItemDto item2 = getItemDto();
        item2.setName("item2");
        itemService.createItem(1, item1);
        itemService.createItem(1, item2);

        assertEquals(2, itemService.getItemSet(1, 0L, 10L).size());
    }

    @Test
    void getItemsByText() {
        userService.addUser(getUserDto());
        itemService.createItem(1, getItemDto());
        ItemDto itemDto = getItemDto();
        itemDto.setId(2L);
        itemDto.setOwner(1L);
        itemDto.setName("otherName");
        itemDto.setDescription("other");

        itemService.createItem(1, itemDto);
        ItemDto expectedItem = getItemDto();
        expectedItem.setId(1L);
        expectedItem.setOwner(1L);

        assertEquals(List.of(expectedItem), itemService.getItemsByText(1, "item", 0L, 10L));
        assertEquals(List.of(itemDto), itemService.getItemsByText(1, "other", 0L, 10L));
    }
}