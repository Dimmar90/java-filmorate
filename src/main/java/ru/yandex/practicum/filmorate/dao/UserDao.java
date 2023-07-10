package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    void setUsersId(User user);

    List<User> findAllUsers();

    Optional<User> findUserById(Long id);

    void addUser(User user);

    void updateUser(User user);

    void addFriend(Long userId, Long friendId);

    List<User> findUserFriends(Long userId);

    List<User> findCommonFriends(Long firstUserId, Long secondUserId);

    void deleteFriend(Long userID, Long friendID);
}
