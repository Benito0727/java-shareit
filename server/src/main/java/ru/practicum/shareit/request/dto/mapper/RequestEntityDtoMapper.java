package ru.practicum.shareit.request.dto.mapper;

import ru.practicum.shareit.request.dto.IncomingItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

public class RequestEntityDtoMapper {

    public static ItemRequestDto getDtoFromEntity(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getAuthor(),
                itemRequest.getCreated());
    }

    public static ItemRequest getEntityFromIncomingDto(IncomingItemRequestDto dto, User author) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setAuthor(author);
        itemRequest.setDescription(dto.getDescription());
        itemRequest.setCreated(LocalDateTime.ofInstant(Instant.now(),
                ZoneId.of(TimeZone.getDefault().getID())));
        return itemRequest;
    }
}
