package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.DBBookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.comment.CommentDtoEntityMapper;
import ru.practicum.shareit.item.dto.comment.IncomingCommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.DBCommentRepository;
import ru.practicum.shareit.item.repository.DBItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.DBUserRepository;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("DBCommentService")
public class DBCommentService implements CommentService {

    private final DBUserRepository userStorage;

    private final DBBookingRepository bookingStorage;

    private final DBItemRepository itemStorage;

    private final DBCommentRepository commentStorage;

    @Autowired
    public DBCommentService(DBUserRepository userStorage,
                            DBBookingRepository bookingStorage,
                            DBItemRepository itemStorage,
                            DBCommentRepository commentStorage) {
        this.userStorage = userStorage;
        this.bookingStorage = bookingStorage;
        this.itemStorage = itemStorage;
        this.commentStorage = commentStorage;
    }

    @Override
    public CommentDto addComment(long userId, long itemId, IncomingCommentDto commentDto) {
        try {
            User author = userStorage.findById(userId).orElseThrow(
                    () -> new NotFoundException(String.format("Не нашли пользователя с ID: %d", userId))
            );
            Item item = itemStorage.findById(itemId).orElseThrow(
                    () -> new NotFoundException(String.format("Не нашли вещи с ID: %d", itemId))
            );
            List<Booking> bookings = bookingStorage.findBookingsByItemIdAndBookerId(itemId, userId)
                    .stream()
                    .filter(booking -> booking.getBooker().getId() == userId &&
                    booking.getEnd().isBefore(LocalDateTime.now()))
                    .collect(Collectors.toList());
            if (bookings.isEmpty()) {
                throw new BadRequestException(
                        String.format("У пользователя нет завершенных бронирований для вещи с ID: %d", itemId)
                );
            }
            Comment comment = new Comment();
            comment.setText(commentDto.getText());
            comment.setAuthorName(author.getName());
            comment.setItem(item);
            comment.setCreatedBy(LocalDateTime.now());

            return CommentDtoEntityMapper.getDtoFromEntity(commentStorage.save(comment));

        } catch (NotFoundException | BadRequestException exception) {
            throw new RuntimeException(exception);
        }
    }

    public ItemDto setCommentsToItems(ItemDto item) {
        Set<Comment> comments = commentStorage.findCommentsByItemId(item.getId());
        Set<CommentDto> commentsDto = new LinkedHashSet<>();
        for (Comment comment : comments) {
            commentsDto.add(CommentDtoEntityMapper.getDtoFromEntity(comment));
        }
        item.setComments(commentsDto);
        return item;
    }
}
