package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.user.dto.UserDto;

@Data
public class CommentDto {

    private String content;

    private UserDto author;

}
