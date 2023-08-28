package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.comment.IncomingCommentDto;

public interface CommentService {

    CommentDto addComment(long userId, long itemId, IncomingCommentDto commentDto);
}
