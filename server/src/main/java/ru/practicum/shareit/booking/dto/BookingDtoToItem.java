package ru.practicum.shareit.booking.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDtoToItem {

    private long id;

    private long bookerId;

    public BookingDtoToItem(BookingDto bookingDto) {
        this.id = bookingDto.getId();
        this.bookerId = bookingDto.getBooker().getId();
    }
}
