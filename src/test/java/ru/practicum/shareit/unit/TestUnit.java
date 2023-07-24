package ru.practicum.shareit.unit;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class TestUnit {

    public static User getUser() {
        User user = new User();
        user.setName("user");
        user.setEmail("user@user.com");
        return user;
    }

    public static Item getItem() {
        Item item = new Item();
        item.setName("item");
        item.setDescription("item description");
        item.setAvailable(true);
        return item;
    }
}
