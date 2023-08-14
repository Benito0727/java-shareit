package ru.practicum.shareit.booking.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;


import java.time.LocalDateTime;

@Data
public class IncomingBookingDto {

    @NotNull
    private Long itemId;

    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;
}
