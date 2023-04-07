package ru.yandex.practicum.filmorate.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ErrorException;
import ru.yandex.practicum.filmorate.exception.ErrorResponse;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Component
@RestController
public class UserService {
    UserStorage userStorage;

    @ExceptionHandler(value = ErrorException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleWrongUserUpdateException(ErrorException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @GetMapping(value = "/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable int userId) {
        List<User> listOfUsers = new ArrayList<>(userStorage.getUsers());
        boolean isUserAdded = false;
        for (User listOfUser : listOfUsers) {
            if (listOfUser.getId() == userId) {
                isUserAdded = true;
                break;
            }
        }
        if (!isUserAdded) {
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("User ID not found")), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(userStorage.getUsers().stream().filter(x -> x.getId() == userId).findFirst(), HttpStatus.OK);
        }
    }
}