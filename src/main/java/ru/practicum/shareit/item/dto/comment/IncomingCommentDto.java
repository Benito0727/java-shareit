package ru.practicum.shareit.item.dto.comment;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class IncomingCommentDto {

    @NotEmpty
    private String text;
}
