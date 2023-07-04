package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFromDb;

import java.util.List;

public interface UserDao {
    UserFromDb findUserById(Long id);

    List<UserFromDb> getAllUsers();

    User findLastUser();

    void addUser(User user);

    void updateUser(User user);

    void addFriend(Long userId, Long friendId);
}
