package ru.practicum.shareit.exception;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice("ru.practicum.shareit")
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(@NotNull final NotFoundException exception) {
        log.error(HttpStatus.NOT_FOUND.toString(), exception.getMessage());
        return new ErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND.toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(@NotNull final ValidationException exception) {
        log.error(HttpStatus.BAD_REQUEST.toString(), exception.getMessage());
        return new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST.toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(@NotNull final BadRequestException exception) {
        log.error(HttpStatus.BAD_REQUEST.toString(), exception.getMessage());
        return new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST.toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(@NotNull final ConflictException exception) {
        log.error(HttpStatus.CONFLICT.toString(), exception.getMessage());
        return new ErrorResponse(exception.getMessage(), HttpStatus.CONFLICT.toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handelForbiddenException(@NotNull final  ForbiddenException exception) {
        log.error(HttpStatus.FORBIDDEN.toString(), exception.getMessage());
        return new ErrorResponse(exception.getMessage(), HttpStatus.FORBIDDEN.toString());
    }
}
