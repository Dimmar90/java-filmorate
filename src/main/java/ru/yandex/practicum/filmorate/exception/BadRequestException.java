package ru.yandex.practicum.filmorate.exception;

public class BadRequestException extends IllegalStateException {
    public BadRequestException(String message) {
        super(message);
    }
}
