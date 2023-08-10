package ru.practicum.shareit.item.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDtoWithBookings extends ItemDto {

    private Long lastBooking;

    private Long nextBooking;

    public ItemDtoWithBookings(Long id,
                               String name,
                               String description,
                               Boolean available,
                               Long owner,
                               Long lastBookingId,
                               Long nextBookingId) {
        super(id, name, description, available, owner);
        this.lastBooking = lastBookingId;
        this.nextBooking = nextBookingId;
    }
}
