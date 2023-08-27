package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.repository.DBBookingRepository;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.DBCommentRepository;
import ru.practicum.shareit.item.repository.DBItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.DBUserRepository;

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
        User user = userRepository.save(getUserEntity());
        User booker = getUserEntity();
        booker.setId(2L);
        booker.setEmail("other@mail.com");
        userRepository.save(booker);

        Item item = itemRepository.save(getItemEntity(user));
        bookingRepository.save(getBookingEntity(booker, item));

        assertEquals(1, item.getId());
        assertEquals("Item", item.getName());
        assertEquals("simple item", item.getDescription());

        Item item1 = itemRepository.findById(1L).get();

        assertEquals(1, item1.getId());
        assertEquals("Item", item1.getName());
        assertEquals("simple item", item1.getDescription());

        CommentDto commentDto = service.addComment(2, 1, getCommentDto());

        assertEquals("new comment", commentDto.getText());
        assertEquals(1, commentDto.getId());
        assertEquals("user", commentDto.getAuthorName());

        Comment comment = commentRepository.findById(1L).get();

        assertEquals(1, comment.getCommentId());
        assertEquals("new comment", comment.getText());
        assertEquals(user.getName(), comment.getAuthorName());
    }
}