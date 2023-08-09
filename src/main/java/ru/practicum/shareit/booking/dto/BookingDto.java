package ru.practicum.shareit.booking.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDtoToBooking;

import java.time.LocalDateTime;


@Data
public class BookingDto {

    private Long id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime start;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
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
