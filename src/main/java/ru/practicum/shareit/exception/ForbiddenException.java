package ru.practicum.shareit.exception;

public class ForbiddenException extends Exception {

    public ForbiddenException() {
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
