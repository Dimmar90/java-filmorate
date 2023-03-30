package ru.yandex.practicum.filmorate.exception;

public class WrongUserUpdateException extends RuntimeException {

    public WrongUserUpdateException(String message) {
        super(message);
    }
}
