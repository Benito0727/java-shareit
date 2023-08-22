package ru.practicum.shareit.unit;

import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

public class TestUnits {

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
        bookingDto.setStart(LocalDateTime.of(2023, 8, 23, 17, 20));
        bookingDto.setEnd(LocalDateTime.of(2023, 8, 24, 17, 0));
        return bookingDto;
    }
}
