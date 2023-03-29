package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    @PostMapping(value = "/users")
    public User create(@RequestBody @Valid User user) {
        validate(user);
        if (users.get(user.getId()) == null) {
            status();
        } else {
            return users.get(user.getId());
        }
        return null;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    void status() {
    }

    public void validate(User user) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (violations.isEmpty() && user.getId() == 0) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            user.setId(id++);
            users.put(user.getId(), user);
        } else if (violations.isEmpty() && users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        }
    }

    @GetMapping(value = "/users")
    public Map<Integer, User> getUsers() {
        log.debug("Get all users: {}", users);
        return users;
    }
}

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



