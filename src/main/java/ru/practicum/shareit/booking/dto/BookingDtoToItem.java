package ru.practicum.shareit.booking.dto;

import lombok.Data;

@Data
public class BookingDtoToItem {

    private long id;

    private long bookerId;

    public BookingDtoToItem(BookingDto bookingDto) {
        this.id = bookingDto.getId();
        this.bookerId = bookingDto.getBooker().getId();
    }
}
