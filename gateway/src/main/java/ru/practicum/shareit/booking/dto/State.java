package ru.practicum.shareit.booking.dto;

import java.util.Arrays;
import java.util.Optional;

public enum State {
    ALL,
    FUTURE,
    CURRENT,
    PAST,
    CANCELED,
    REJECTED;

    public static Optional<State> getStateFrom(java.lang.String stringStatus) {
        return Arrays.stream(State.values())
                .filter(status -> status.toString().equals(stringStatus))
                .findAny();
    }
}
