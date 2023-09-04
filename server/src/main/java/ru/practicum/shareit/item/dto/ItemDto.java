package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingDtoToItem;
import ru.practicum.shareit.item.dto.comment.CommentDto;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ItemDto {

    private Long id;
    private String name;

    private String description;

    private Boolean available;

    private Long owner;

    private Set<CommentDto> comments;

    private BookingDtoToItem nextBooking;

    private BookingDtoToItem lastBooking;

    private Long requestId;

    public ItemDto(Long id,
                   String name,
                   String description,
                   Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
