package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Data
@Validated
public class IncomingBookingDto {

    @NotNull
    private Long itemId;

    @NotNull
    private LocalDate start;

    @NotNull
    private LocalDate end;
}
