package ru.practicum.shareit.exception;

import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice("ru.practicum.shareit")
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(@NotNull final BadRequestException exception) {
        log.error(HttpStatus.BAD_REQUEST.toString(), exception.getMessage());
        return new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST.toString());
    }
}
