package ru.practicum.shareit.booking.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class IncomingBookingDto {

    @NotNull
    private Long itemId;

    @NotNull
    private LocalDate start;

    @NotNull
    private LocalDate end;
}
