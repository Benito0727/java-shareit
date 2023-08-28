package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoToRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.DBItemRepository;
import ru.practicum.shareit.request.dto.IncomingItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.mapper.RequestEntityDtoMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestDBRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.DBUserRepository;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.dto.ItemEntityDtoMapper.getItemDtoToRequest;
import static ru.practicum.shareit.request.dto.mapper.RequestEntityDtoMapper.getEntityFromIncomingDto;

@Service
public class ItemRequestDBService implements ItemRequestService {

    private final ItemRequestDBRepository storage;

    private final DBUserRepository userStorage;

    private final DBItemRepository itemStorage;

    @Autowired
    public ItemRequestDBService(ItemRequestDBRepository storage, DBUserRepository userStorage, DBItemRepository itemStorage) {
        this.storage = storage;
        this.userStorage = userStorage;
        this.itemStorage = itemStorage;
    }

    @Override
    public ItemRequestDto getRequestById(Long userId, Long requestId) {
        checkUser(userId);
        ItemRequestDto requestDto = RequestEntityDtoMapper.getDtoFromEntity(checkItemRequest(requestId));
        Set<Item> items = itemStorage.findItemsByRequestId(requestId);
        Set<ItemDtoToRequest> itemsDto = new LinkedHashSet<>();
        for (Item item : items) {
            itemsDto.add(getItemDtoToRequest(item));
        }

        requestDto.setItems(itemsDto);

        return requestDto;
    }

    @Override
    public Set<ItemRequestDto> getUserRequests(Long userId) {
        checkUser(userId);
        Set<ItemRequestDto> requestsDto = new LinkedHashSet<>();
        for (ItemRequest request : storage.findAllByAuthorId(userId)) {
            Set<Item> items = itemStorage.findItemsByRequestId(request.getId());
            Set<ItemDtoToRequest> itemsDto = new LinkedHashSet<>();
            for (Item item : items) {
                itemsDto.add(getItemDtoToRequest(item));
            }
            ItemRequestDto requestDto = RequestEntityDtoMapper.getDtoFromEntity(request);

            requestDto.setItems(itemsDto);
            requestsDto.add(requestDto);
        }
        return requestsDto.stream()
                .sorted(Comparator.comparing(ItemRequestDto::getCreated))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Set<ItemRequestDto> getOtherUsersRequests(Long userId, Long from, Long size) {
        checkUser(userId);
        Set<ItemRequestDto> requestsDto = new LinkedHashSet<>();
        if (from == null && size == null) {
            from = 0L;
            size = 10L;
        }
        for (ItemRequest itemRequest : storage.findAllByAuthorIdIsNot(userId,
                PageRequest.of(from.intValue(), size.intValue(), Sort.by("created").descending()))) {
            ItemRequestDto requestDto = RequestEntityDtoMapper.getDtoFromEntity(itemRequest);
            Set<Item> items = itemStorage.findItemsByRequestId(itemRequest.getId());
            Set<ItemDtoToRequest> itemsDto = new LinkedHashSet<>();
            for (Item item : items) {
                itemsDto.add(getItemDtoToRequest(item));
            }
            requestDto.setItems(itemsDto);
            requestsDto.add(requestDto);
        }
        return new LinkedHashSet<>(requestsDto);
    }

    @Override
    public ItemRequestDto addRequest(Long userId, IncomingItemRequestDto incomingItemRequest) {
        User author = checkUser(userId);
        ItemRequest itemRequest = getEntityFromIncomingDto(incomingItemRequest, author);
        return RequestEntityDtoMapper.getDtoFromEntity(storage.save(itemRequest));
    }

    private User checkUser(Long userId) {
        try {
            return userStorage.findById(userId).orElseThrow(
                    () -> new NotFoundException(String.format("Не нашли пользвателя с ID: %d", userId))
            );
        } catch (NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    private ItemRequest checkItemRequest(Long requestId) {
        try {
            return storage.findById(requestId).orElseThrow(
                    () -> new NotFoundException(String.format("Не нашли запроса с ID: %d", requestId))
            );
        } catch (NotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }
}
