package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

public class BookingDtoEntityMapper {

    public static Booking getEntityFromIncomingDto(IncomingBookingDto dto) {
        return new Booking(dto.getStart(), dto.getEnd());
    }

    public static BookingDto getDtoFromEntity(Booking booking, Item item) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                booking.getBooker().getId(),
                item.getId(),
                item.getName()
        );
    }

    public static BookingDtoToItem getBookingDtoToItem(BookingDto bookingDto) {
        return new BookingDtoToItem(bookingDto.getId(),
                                    bookingDto.getBooker().getId());
    }
}
