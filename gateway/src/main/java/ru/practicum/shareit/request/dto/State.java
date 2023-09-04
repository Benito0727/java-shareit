package ru.practicum.shareit.request.dto;

import java.util.Arrays;
import java.util.Optional;

public enum State {
    ALL;

    public static Optional<State> getStateFrom(String stringState) {
        return Arrays.stream(State.values())
                .filter(state -> state.name().equals(stringState.toUpperCase()))
                .findAny();
    }
}
