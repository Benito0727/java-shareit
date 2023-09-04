package ru.practicum.shareit.item.dto.comment;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
