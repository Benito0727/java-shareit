package ru.practicum.shareit.unit;

import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.IncomingItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class TestUnits {

    public final static String USER_1_JSON = "{\n" +
            "    \"name\": \"user\",\n" +
            "    \"email\": \"user@user.com\"\n" +
            "}";

    public final static String USER_2_JSON = "{\n" +
            "    \"name\": \"user\",\n" +
            "    \"email\": \"other@user.com\"\n" +
            "}";

    public final static String USER_3_JSON = "{\n" +
            "    \"name\": \"user\",\n" +
            "    \"email\": \"other@mail.com\"\n" +
            "}";

    public final static String USER_JSON_INVALID_EMAIL = "{\n" +
            "    \"name\": \"user\",\n" +
            "    \"email\": \"user.com\"\n" +
            "}";

    public final static String USER_JSON_WITHOUT_EMAIL = "{\n" +
            "    \"name\": \"user\"\n" +
            "}";

    public final static String JSON_TO_UPDATE_USER = "{\n" +
            "    \"name\": \"update\",\n" +
            "    \"email\": \"update@user.com\"\n" +
            "}";

    public final static String JSON_TO_UPDATE_USER_ONLY_EMAIL = "{\n" +
            "    \"email\": \"updateName@user.com\"\n" +
            "}";

    public final static String JSON_TO_UPDATE_USER_ONLY_NAME = "{\n" +
            "    \"name\": \"updateName\"\n" +
            "}";

    public final static String ITEM_1_JSON = "{\n" +
            "    \"name\": \"Дрель\",\n" +
            "    \"description\": \"Простая дрель\",\n" +
            "    \"available\": true\n" +
            "}";

    public final static String ITEM_2_JSON = "{\n" +
            "    \"name\": \"Отвертка\",\n" +
            "    \"description\": \"Аккумуляторная отвертка\",\n" +
            "    \"available\": true\n" +
            "}";

    public final static String ITEM_3_JSON = "{\n" +
            "    \"name\": \"Клей Момент\",\n" +
            "    \"description\": \"Тюбик суперклея марки Момент\",\n" +
            "    \"available\": true\n" +
            "}";

    public final static String JSON_TO_UPDATE_ITEM = "{\n" +
            "    \"id\": 1,\n" +
            "    \"name\": \"Дрель+\",\n" +
            "    \"description\": \"Аккумуляторная дрель\",\n" +
            "    \"available\": false\n" +
            "}";

    public final static String JSON_TO_UPDATE_ITEM_DESCRIPTION_ONLY = "{\n" +
            "   \"description\": \"Аккумуляторная дрель + аккумулятор\"\n" +
            "}";

    public final static String JSON_TO_UPDATE_ITEM_NAME_ONLY = "{\n" +
            "   \"name\": \"Аккумуляторная дрель\"\n" +
            "}";

    public final static String JSON_TO_UPDATE_ITEM_AVAILABLE = "{\n" +
            "    \"available\": true\n" +
            "}";


    public static User getUserEntity() {
        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@user.com");
        return user;
    }

    public static UserDto getUserDto() {
        UserDto userDto = new UserDto();
        userDto.setName("user");
        userDto.setEmail("user@email.com");
        return userDto;
    }

    public static ItemDto getItemDto() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("item");
        itemDto.setDescription("item description");
        itemDto.setAvailable(true);
        return itemDto;
    }

    public static IncomingBookingDto getBookingDto() {
        IncomingBookingDto bookingDto = new IncomingBookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        return bookingDto;
    }

    public static IncomingItemRequestDto getItemRequestDto() {
        IncomingItemRequestDto itemRequest = new IncomingItemRequestDto();
        itemRequest.setDescription("description");
        return itemRequest;
    }
}
