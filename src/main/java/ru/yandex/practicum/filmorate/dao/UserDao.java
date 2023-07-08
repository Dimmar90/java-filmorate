package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDao {
    User findUserById(Long id);

    List<User> getAllUsers();

    User findLastUser();

    void addUser(User user);

    void updateUser(User user);

    void addFriend(Long userId, Long friendId);

    List<User> getUserFriends(Long userId);

    List<User> getCommonFriends(Long userId, Long friendId);

    void deleteFriend(Long userID, Long friendID);
}
