package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;

public class BookingDtoEntityMapper {

    public static Booking getEntityFromIncomingDto(IncomingBookingDto dto) {
        return new Booking(dto.getItemId(), dto.getStart(), dto.getEnd());
    }

    public static BookingDto getDtoFromEntity(Booking booking) {
        return new BookingDto(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                booking.getBooker(),
                booking.getItemId(),
                booking.getItemName());
    }
}
