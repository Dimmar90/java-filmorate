package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.WrongIdException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
public class UserService {

    private UserDao userDao;
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        userDao.setUsersId(user);
        userDao.addUser(user);
        log.debug("User Added");
    }

    public User getUserById(long userId) {
        wrongUserIdException(userId);
        log.debug("Get User By Id");
        return userDao.findUserById(userId).get();
    }

    public List<User> getAllUsers() {
        log.debug("Get All Users");
        return userDao.findAllUsers();
    }


    public void updateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        wrongUserIdException(user.getId());
        log.debug("User Updated");
        userDao.updateUser(user);
    }

    public void addFriend(long userId, long friendId) {
        wrongUserIdException(userId);
        wrongUserIdException(friendId);
        userDao.addFriend(userId, friendId);
        log.debug("Friend Added");
    }

    public List<User> getUsersFriends(long userId) {
        wrongUserIdException(userId);
        return userDao.findUserFriends(userId);

    }

    public List<User> getCommonFriends(long userId, long friendId) {
        wrongUserIdException(userId);
        wrongFriendIdException(friendId);
        log.debug("Get Common Friends");
        return userDao.findCommonFriends(userId, friendId);
    }

    public void deleteFriend(long userId, long friendId) {
        wrongUserIdException(userId);
        wrongFriendIdException(friendId);
        userDao.deleteFriend(userId, friendId);
        userDao.deleteFriend(friendId, userId);
        log.debug("Friend Deleted");
    }

    public void wrongUserIdException(long userId) {
        if (userDao.findUserById(userId).isEmpty()) {
            String msg = "User With This Id Does Not Exist";
            log.warn(msg);
            throw new WrongIdException(msg);
        }
    }

    public void wrongFriendIdException(long friendId) {
        if (userDao.findUserById(friendId).isEmpty()) {
            String msg = "Friend With This Id Does Not Exist";
            log.warn(msg);
            throw new WrongIdException(msg);
        }
    }
}