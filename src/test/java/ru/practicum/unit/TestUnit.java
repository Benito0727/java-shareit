package ru.practicum.unit;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

public class TestUnit {

    public static UserDto getUser() {
        UserDto user = new UserDto();
        user.setName("user");
        user.setEmail("user@user.com");
        return user;
    }

    public static ItemDto getItem() {
        ItemDto item = new ItemDto();
        item.setName("item");
        item.setDescription("item description");
        item.setAvailable(true);
        return item;
    }
}
