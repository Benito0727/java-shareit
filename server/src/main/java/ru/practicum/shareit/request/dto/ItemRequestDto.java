package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemDtoToRequest;
import ru.practicum.shareit.user.dto.UserToItemRequestDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {

    private Long id;

    private String description;

    private UserToItemRequestDto author;

    private LocalDateTime created;

    private Set<ItemDtoToRequest> items;

    public ItemRequestDto(Long id, String description, User author, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.author = new UserToItemRequestDto(author.getId(), author.getName());
        this.created = created;
    }
}
