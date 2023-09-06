package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.IncomingItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Set;

public interface ItemRequestService {

    ItemRequestDto getRequestById(Long userId, Long requestId);

    Set<ItemRequestDto> getUserRequests(Long userId);

    Set<ItemRequestDto> getOtherUsersRequests(Long userId, Long from, Long size);

    ItemRequestDto addRequest(Long userId, IncomingItemRequestDto incomingItemRequest);
}
