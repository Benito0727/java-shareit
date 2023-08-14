package ru.practicum.shareit.booking.dto;

import lombok.Data;

@Data
public class Booker {

    private Long id;

    public Booker(Long bookerId) {
        this.id = bookerId;
    }
}
