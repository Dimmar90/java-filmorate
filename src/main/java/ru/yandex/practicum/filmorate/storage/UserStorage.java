package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Deque;
import java.util.List;
import java.util.Map;

public interface UserStorage {
    void addUser(User user);

    void updateUser(User user);

    User getUserById(long id);

    Map<Long, User> getAllUsers();

    void addFriend(User user, User friend);

    Deque<User> getUserFriendList(User user);

    List<User> getCommonListOfFriends(User user, User usersFriend);

    void deleteFriend(User user, User friend);
}
