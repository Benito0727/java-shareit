package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.repository.DBBookingRepository;
import ru.practicum.shareit.booking.service.DBBookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.repository.DBCommentRepository;
import ru.practicum.shareit.item.repository.DBItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.DBUserRepository;
import ru.practicum.shareit.user.service.DBUserService;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.unit.TestUnits.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DBCommentServiceTest {

    private DBCommentService service;

    @Autowired
    private DBCommentRepository commentRepository;

    @Autowired
    private DBUserRepository userRepository;

    @Autowired
    private DBItemRepository itemRepository;

    @Autowired
    private DBBookingRepository bookingRepository;

    @Autowired
    private DBItemService itemService;

    @Autowired
    private DBUserService userService;

    @Autowired
    private DBBookingService bookingService;

    @BeforeEach
    void setUp() {
        this.service = new DBCommentService(
                userRepository,
                bookingRepository,
                itemRepository,
                commentRepository
        );
    }

    @Test
    void addComment() {
        UserDto user = userService.addUser(getUserDto());
        UserDto booker = getUserDto();
        booker.setId(2L);
        booker.setEmail("other@mail.com");
        userService.addUser(booker);

        ItemDto item = itemService.createItem(1, getItemDto());
        bookingService.addBooking(2L, getBookingDtoForComment());

        assertEquals(1, item.getId());
        assertEquals("item", item.getName());
        assertEquals("item description", item.getDescription());

        CommentDto commentDto = service.addComment(2L, 1L, getCommentDto());

        assertEquals("new comment", commentDto.getText());
        assertEquals(1, commentDto.getId());
        assertEquals("user", commentDto.getAuthorName());

        Comment comment = commentRepository.findById(1L).get();

        assertEquals(1, comment.getCommentId());
        assertEquals("new comment", comment.getText());
        assertEquals(user.getName(), comment.getAuthorName());
    }
}