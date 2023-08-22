package ru.practicum.shareit.user.dto;

import lombok.Data;

@Data
public class UserToItemRequestDto {

    private final Long id;

    private final String name;
}
