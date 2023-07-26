package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.constraints.NotNull;

@RestControllerAdvice("ru.practicum.shareit")
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(@NotNull final NotFoundException exception) {
        log.error(HttpStatus.NOT_FOUND.toString(), exception.getMessage());
        return new ErrorResponse(HttpStatus.NOT_FOUND.toString(), exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(@NotNull final ValidationException exception) {
        log.error(HttpStatus.BAD_REQUEST.toString(), exception.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(@NotNull final ConflictException exception) {
        log.error(HttpStatus.CONFLICT.toString(), exception.getMessage());
        return new ErrorResponse(HttpStatus.CONFLICT.toString(), exception.getMessage());
    }

}
