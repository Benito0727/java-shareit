package ru.practicum.shareit.item.dto.comment;

import ru.practicum.shareit.item.model.Comment;

public class CommentDtoEntityMapper {
    public static CommentDto getDtoFromEntity(Comment comment) {
        CommentDto commentDto = new CommentDto(comment.getCommentId(),
                comment.getText(),
                comment.getAuthorName());
        commentDto.setCreated(comment.getCreatedBy());
        return commentDto;
    }
}
