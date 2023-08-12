package ru.practicum.shareit.item.dto.comment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {

    private Long id;

    private String text;

    private String authorName;

    private LocalDateTime created;

    public CommentDto(Long id,
                      String text,
                      String authorName) {
        this.id = id;
        this.text = text;
        this.authorName = authorName;
    }
}
