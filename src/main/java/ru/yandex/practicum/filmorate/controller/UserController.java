package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.*;
import java.util.*;

@RestController
public class UserController {
    private final List<User> users = new ArrayList<>();
    private int id = 0;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping(value = "/users")
    public User create(@RequestBody @Valid User user) {
        try {
            if (user.getName() == null) {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            user.setName(user.getLogin());
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(++id);
        users.add(user);
        return user;
    }

    @PutMapping(value = "/users")
    public ResponseEntity<?> updateUser(@RequestBody @Valid User user) {
        if (!users.contains(user)) {
            return new ResponseEntity<>("Wrong update user", HttpStatus.NOT_FOUND);
        } else {
            users.add(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/users")
    public List<User> getUsers() {
        log.debug("Get all users: {}", users);
        return users;
    }
}

//    public void validate(User user) {
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        Validator validator = factory.getValidator();
//        Set<ConstraintViolation<User>> violations = validator.validate(user);
//        if (violations.isEmpty()) {
//            user.setId(id++);
//            users.put(user.getId(), user);
//        }
//    }

//    public User userValidation(User user) {
//        try {
//            if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
//                throw new ValidationException("Incorrect user email");
//            }
//        } catch (ValidationException e) {
//            log.error(e.getMessage());
//            return null;
//        }
//        try {
//            if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
//                throw new ValidationException("Incorrect user login");
//            }
//        } catch (ValidationException e) {
//            log.error(e.getMessage());
//            return null;
//        }
//        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
//            user.setName(user.getLogin());
//        }
//        try {
//            if (user.getBirthday().isAfter(LocalDateTime.now().toLocalDate())) {
//                throw new ValidationException("Incorrect user birthday");
//            }
//        } catch (ValidationException e) {
//            log.error(e.getMessage());
//            return null;
//        }
//
//        if (users.containsKey(user.getId())) {
//            users.put(user.getId(), user);
//            log.debug("User updated: {}", user.getName());
//        } else {
//            user.setId(id++);
//            users.put(user.getId(), user);
//            log.debug("User Added: {}", user.getName());
//        }
//        return user;
//    }



