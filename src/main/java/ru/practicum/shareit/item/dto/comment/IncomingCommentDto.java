package ru.practicum.shareit.item.dto.comment;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class IncomingCommentDto {

    @NotEmpty
    private String text;
}
