package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserStorage {
    void addUser(User user);

    void updateUser(User user);

    User getUserById(long id);

    Map<Long, User> getAllUsers();

    void addFriend(User user, User friend);

    Set<User> getUserFriendList(User user);

    List<User> getCommonListOfFriends(User user, User usersFriend);

    void deleteFriend(User user, User friend);
}
