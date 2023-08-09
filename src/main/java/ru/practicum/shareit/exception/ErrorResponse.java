package ru.practicum.shareit.exception;

public class ErrorResponse {

    private String error;

    private String status;

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.status = description;
    }

    public ErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public String getStatus() {
        return status;
    }
}
