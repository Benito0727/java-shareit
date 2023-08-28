package ru.practicum.shareit.unit;

import org.springframework.test.web.servlet.RequestBuilder;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.comment.IncomingCommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.IncomingItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class TestUnits {

    public static final String USER_1_JSON = "{\n" +
            "    \"name\": \"user\",\n" +
            "    \"email\": \"user@user.com\"\n" +
            "}";

    public static final String USER_2_JSON = "{\n" +
            "    \"name\": \"user\",\n" +
            "    \"email\": \"other@user.com\"\n" +
            "}";

    public static final String USER_3_JSON = "{\n" +
            "    \"name\": \"user\",\n" +
            "    \"email\": \"other@mail.com\"\n" +
            "}";

    public static final String USER_JSON_INVALID_EMAIL = "{\n" +
            "    \"name\": \"user\",\n" +
            "    \"email\": \"user.com\"\n" +
            "}";

    public static final String USER_JSON_WITHOUT_EMAIL = "{\n" +
            "    \"name\": \"user\"\n" +
            "}";

    public static final String JSON_TO_UPDATE_USER = "{\n" +
            "    \"name\": \"update\",\n" +
            "    \"email\": \"update@user.com\"\n" +
            "}";

    public static final String JSON_TO_UPDATE_USER_ONLY_EMAIL = "{\n" +
            "    \"email\": \"updateName@user.com\"\n" +
            "}";

    public static final String JSON_TO_UPDATE_USER_ONLY_NAME = "{\n" +
            "    \"name\": \"updateName\"\n" +
            "}";

    public static final String ITEM_1_JSON = "{\n" +
            "    \"name\": \"Дрель\",\n" +
            "    \"description\": \"Простая дрель\",\n" +
            "    \"available\": true\n" +
            "}";

    public static final String ITEM_2_JSON = "{\n" +
            "    \"name\": \"Отвертка\",\n" +
            "    \"description\": \"Аккумуляторная отвертка\",\n" +
            "    \"available\": true\n" +
            "}";

    public static final String ITEM_TO_ITEM_REQUEST_JSON = "{\n" +
            "    \"name\": \"Щётка для обуви\",\n" +
            "    \"description\": \"Стандартная щётка для обуви\",\n" +
            "    \"available\": true,\n" +
            "    \"requestId\": 1\n" +
            "}";

    public static final String JSON_TO_UPDATE_ITEM = "{\n" +
            "    \"id\": 1,\n" +
            "    \"name\": \"Дрель+\",\n" +
            "    \"description\": \"Аккумуляторная дрель\",\n" +
            "    \"available\": false\n" +
            "}";

    public static final String JSON_TO_UPDATE_ITEM_DESCRIPTION_ONLY = "{\n" +
            "   \"description\": \"Аккумуляторная дрель + аккумулятор\"\n" +
            "}";

    public static final String JSON_TO_UPDATE_ITEM_NAME_ONLY = "{\n" +
            "   \"name\": \"Аккумуляторная дрель\"\n" +
            "}";

    public static final String JSON_TO_UPDATE_ITEM_AVAILABLE = "{\n" +
            "    \"available\": true\n" +
            "}";

    public static final String JSON_ITEM_REQUEST_1 = "{\n" +
            "    \"description\": \"Хотел бы воспользоваться щёткой для обуви\"\n" +
            "}";

    public static final String JSON_ITEM_REQUEST_WITH_EMPTY_DESCRIPTION = "{\n" +
            "    \"description\": null\n" +
            "}";

    public static final String BOOKING_1_JSON = "{\n" +
            "    \"itemId\": 1,\n" +
            "    \"start\": \"2023-10-27T14:00:00\",\n" +
            "    \"end\": \"2023-10-28T14:00:00\"\n" +
            "}";

    public static final String COMMENT_1_JSON = "{\n" +
            "    \"text\": \"Comment for item 1\"\n" +
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

    public static IncomingBookingDto getBookingDtoForComment() {
        IncomingBookingDto dto = new IncomingBookingDto();
        dto.setItemId(1L);
        dto.setStart(LocalDateTime.now().minusMinutes(5));
        dto.setEnd(LocalDateTime.now().minusMinutes(2));
        return dto;
    }

    public static IncomingItemRequestDto getItemRequestDto() {
        IncomingItemRequestDto itemRequest = new IncomingItemRequestDto();
        itemRequest.setDescription("description");
        return itemRequest;
    }

    public static RequestBuilder getUser1PostRequest() {
        return post("/users")
                .contentType(APPLICATION_JSON)
                .content(USER_1_JSON);
    }

    public static RequestBuilder getUser2PostRequest() {
        return post("/users")
                .contentType(APPLICATION_JSON)
                .content(USER_2_JSON);
    }

    public static RequestBuilder getIR1PostRequest() {
        return post("/requests")
                .contentType(APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1)
                .content(JSON_ITEM_REQUEST_1);
    }

    public static RequestBuilder getIR2PostRequest() {
        return post("/requests")
                .contentType(APPLICATION_JSON)
                .header("X-Sharer-User-Id", 2)
                .content(JSON_ITEM_REQUEST_1);
    }

    public static RequestBuilder getItem1PostRequest() {
        return post("/items")
                .contentType(APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1)
                .content(ITEM_1_JSON);
    }

    public static Comment getCommentEntity(Item item) {
        Comment comment = new Comment();
        comment.setCommentId(1L);
        comment.setText("comment text");
        comment.setAuthorName("user");
        comment.setCreatedBy(LocalDateTime.now());
        comment.setItem(item);
        return comment;
    }

    public static IncomingCommentDto getCommentDto() {
        IncomingCommentDto dto = new IncomingCommentDto();
        dto.setText("new comment");
        return dto;
    }

    public static Item getItemEntity(User user) {
        Item item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setDescription("simple item");
        item.setAvailable(true);
        item.setOwner(user);
        return item;
    }

    public static Booking getBookingEntity(User booker, Item item) {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().minusMinutes(5));
        booking.setEnd(LocalDateTime.now().minusMinutes(3));
        booking.setStatus(Status.WAITING);
        booking.setBooker(booker);
        booking.setItem(item);
        return booking;
    }

}
