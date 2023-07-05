package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserWithStatus;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final UserDao userDao;

    public InMemoryUserStorage(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void addUser(User user) {
        if (userDao.findLastUser() == null) {
            user.setId(1);
            userDao.addUser(user);
        } else {
            user.setId(userDao.findLastUser().getId() + 1);
            userDao.addUser(user);
        }
    }

    @Override
    public User findUserById(Long userId) {
        return userDao.findUserById(userId);
    }

    @Override
    public List<User> findAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    @Override
    public void addFriend(Long userID, Long friendID) {
        userDao.addFriend(userID, friendID);
    }

    @Override
    public List<UserWithStatus> findUsersFriends(Long userId) {
        return userDao.getUserFriends(userId);
    }

    @Override
    public List<User> findCommonFriends(Long userId, Long friendId) {
        return userDao.getCommonFriends(userId, friendId);
    }

    @Override
    public void deleteFriendId(Long userID, Long friendID) {
        userDao.deleteFriend(userID,friendID);
    }


}