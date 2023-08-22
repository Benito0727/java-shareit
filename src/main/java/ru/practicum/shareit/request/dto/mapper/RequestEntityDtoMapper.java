package ru.practicum.shareit.request.dto.mapper;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

public class RequestEntityDtoMapper {

    public static ItemRequestDto getDtoFromEntity(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getAuthor(),
                itemRequest.getCreated());
    }
}
