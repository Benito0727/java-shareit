package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDtoToBooking;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Status status;

    private Booker booker;

    private ItemDtoToBooking item;

    public BookingDto(Long id, LocalDateTime start, LocalDateTime end, Status status, Long bookerId, Long itemId, String itemName) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.status = status;
        this.booker = new Booker(bookerId);
        this.item = new ItemDtoToBooking(itemId, itemName);
    }
}
