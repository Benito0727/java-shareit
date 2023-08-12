package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoToItem;
import ru.practicum.shareit.item.dto.comment.CommentDto;

import java.util.Set;

@Data
public class ItemDto {

    private Long id;
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Boolean available;

    private Long owner;

    private Set<CommentDto> comments;

    private BookingDtoToItem nextBooking;

    private BookingDtoToItem lastBooking;

    public ItemDto() {
    }

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
