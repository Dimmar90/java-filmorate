package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;

public interface UserStorage {
    User createUser(@RequestBody @Valid User user);

    ResponseEntity<?> updateUser(@RequestBody @Valid User user);

    Collection<User> getUsers();
}
