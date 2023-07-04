package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFromDb;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
   // void addUser(User user, Long lastId);
    void addUser(User user);

    UserFromDb findUserById(Long userId);

    List<UserFromDb> findAllUsers();

     void updateUser(User user);

    void addFriend(Long userID, Long friendID);
//
//    List<User> findUsersFriends(Long userId);
//
//    List<User> findCommonFriends(Long userId, Long friendId);
//
//    void deleteFriendId(Long userID, Long friendID);
//
//    boolean isUsersFriend(Long userId, Long friendId);
}
