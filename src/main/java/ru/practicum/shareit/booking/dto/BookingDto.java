package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDate;

@Data
public class BookingDto {

    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private Status status;

    private Long bookerId;

    private Long itemId;

    private String itemName;

    public BookingDto(Long id, LocalDate startDate, LocalDate endDate, Status status, Long bookerId, Long itemId, String itemName) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.bookerId = bookerId;
        this.itemId = itemId;
        this.itemName = itemName;
    }
}
