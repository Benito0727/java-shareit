package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.DBBookingRepository;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.DBCommentRepository;
import ru.practicum.shareit.item.repository.DBItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.DBUserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.unit.TestUnits.*;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DBCommentServiceTest {

    @InjectMocks
    private DBCommentService service;

    @Mock
    private DBCommentRepository commentRepository;

    @Mock
    private DBUserRepository userRepository;

    @Mock
    private DBItemRepository itemRepository;

    @Mock
    private DBBookingRepository bookingRepository;

    @Test
    void addComment() {
        User user = new User(
                2L,
                "user",
                "user@mail.com"
        );
        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));

        Item item = new Item(
                1L,
                "item",
                "description",
                true);

        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));

        Booking booking = new Booking(
                1L,
                LocalDateTime.now().minusMinutes(2),
                LocalDateTime.now().minusMinutes(1),
                Status.APPROVED,
                user,
                item
                );

        when(bookingRepository.findBookingsByItemIdAndBookerId(1L, 2L))
                .thenReturn(List.of(booking));

        when(commentRepository.save(any()))
                .thenReturn(new Comment(
                        1L,
                        "text",
                        "user",
                        LocalDateTime.now(),
                        item
                ));

        CommentDto commentDto = service.addComment(2, 1, getCommentDto());

        assertEquals(1, commentDto.getId());
        assertEquals("text", commentDto.getText());
        assertEquals("user", commentDto.getAuthorName());
    }
}