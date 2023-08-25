package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.service.DBItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.DBUserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.unit.TestUnits.*;


@Transactional
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DBBookingServiceTest {

    private final DBBookingService bookingService;
    private final DBItemService itemService;
    private final DBUserService userService;


    @Test
    void addBookingOrThrowsException() {
        assertThrows(RuntimeException.class, () -> bookingService.addBooking(1L, getBookingDto()));

        userService.addUser(getUserDto());
        itemService.createItem(1, getItemDto());
        UserDto user2 = getUserDto();
        user2.setEmail("other@mail.com");
        userService.addUser(user2);
        BookingDto bookingDto = bookingService.addBooking(2L, getBookingDto());

        IncomingBookingDto dtoWithoutItemId = getBookingDto();
        dtoWithoutItemId.setItemId(null);

        IncomingBookingDto dtoIncorrectStart = getBookingDto();
        dtoIncorrectStart.setStart(LocalDateTime.now().plusDays(3));

        IncomingBookingDto dtoNullsEnd = getBookingDto();
        dtoNullsEnd.setEnd(null);

        assertThrows(RuntimeException.class, () -> bookingService.addBooking(1L, getBookingDto()));
        assertThrows(RuntimeException.class, () -> bookingService.addBooking(2L, dtoWithoutItemId));
        assertThrows(RuntimeException.class, () -> bookingService.addBooking(2L, dtoIncorrectStart));
        assertThrows(RuntimeException.class, () -> bookingService.addBooking(2L, dtoNullsEnd));

        assertEquals(1L, bookingDto.getId());
        assertEquals(1L, bookingDto.getItem().getId());
        assertEquals(2L, bookingDto.getBooker().getId());
        assertTrue(bookingDto.getStart().isBefore(bookingDto.getEnd()));
        assertEquals(Status.WAITING, bookingDto.getStatus());
    }

    @Test
    void setApprovedOrThrowsException() {
        userService.addUser(getUserDto());
        UserDto user2 = getUserDto();
        user2.setEmail("other@mail.com");
        userService.addUser(user2);

        itemService.createItem(1, getItemDto());

        bookingService.addBooking(2L, getBookingDto());

        assertThrows(RuntimeException.class, () -> bookingService.setApproved(2L, 1L, true));
        bookingService.setApproved(1L, 1L, true);
        assertEquals(Status.APPROVED, bookingService.findByBookingId(2L, 1L).getStatus());

        bookingService.setApproved(1L, 1L, false);
        assertEquals(Status.REJECTED, bookingService.findByBookingId(2L, 1L).getStatus());
    }

    @Test
    void findAllByUserId() {
        userService.addUser(getUserDto());
        UserDto user2 = getUserDto();
        user2.setEmail("other@mail.com");
        userService.addUser(user2);

        itemService.createItem(1, getItemDto());

        bookingService.addBooking(2L, getBookingDto());
        bookingService.addBooking(2L, getBookingDto());

        assertNotNull(bookingService.findAllByUserId(2L, "all", 0, 10));
    }

    @Test
    void findBookingsToItemsOwner() {
        userService.addUser(getUserDto());
        UserDto user2 = getUserDto();
        user2.setEmail("other@mail.com");
        userService.addUser(user2);

        itemService.createItem(1, getItemDto());

        bookingService.addBooking(2L, getBookingDto());
        bookingService.addBooking(2L, getBookingDto());

        assertFalse(bookingService.findBookingsToItemsOwner(1L, "all", 0, 10).getSource().isEmpty());
        assertTrue(bookingService.findBookingsToItemsOwner(2L, "all", 0, 10).getSource().isEmpty());
    }

    @Test
    void findByBookingIdOrThrowsException() {
        userService.addUser(getUserDto());
        UserDto user2 = getUserDto();
        user2.setEmail("other@mail.com");
        userService.addUser(user2);
        UserDto user3 = getUserDto();
        user3.setEmail("other@yandex.ru");
        userService.addUser(user3);

        itemService.createItem(1, getItemDto());

        bookingService.addBooking(2L, getBookingDto());

        assertThrows(RuntimeException.class, () -> bookingService.findByBookingId(2L, 2L));
        assertThrows(RuntimeException.class, () -> bookingService.findByBookingId(3L, 1L));

        assertEquals(1, bookingService.findByBookingId(2L, 1L).getId());
    }

    @Test
    void findAllByItemId() {
        userService.addUser(getUserDto());
        UserDto user2 = getUserDto();
        user2.setEmail("other@mail.com");
        userService.addUser(user2);

        itemService.createItem(1, getItemDto());
        itemService.createItem(2, getItemDto());

        bookingService.addBooking(2L, getBookingDto());
        bookingService.addBooking(2L, getBookingDto());

        IncomingBookingDto bookingDto = getBookingDto();
        bookingDto.setItemId(2L);

        bookingService.addBooking(1L, bookingDto);
        bookingService.addBooking(1L, bookingDto);
        bookingService.addBooking(1L, bookingDto);

        assertEquals(2, bookingService.findAllByItemId(1L).size());
        assertEquals(3, bookingService.findAllByItemId(2L).size());
    }
}