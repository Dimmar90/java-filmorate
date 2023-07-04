package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ErrorException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFromDb;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class UserService {

    private UserStorage userStorage;
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.debug("User Added");
        userStorage.addUser(user);
    }

    public UserFromDb getUserById(long userId) {
        if (userStorage.findUserById(userId) == null) {
            String msg = "User With This Id Does Not Exist";
            log.warn(msg);
            throw new ErrorException(msg);
        }
        log.debug("Get User By Id");
        return userStorage.findUserById(userId);
    }

    public List<UserFromDb> getAllUsers() {
        log.debug("Get All Users");
        return userStorage.findAllUsers();
    }


    public void updateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (userStorage.findUserById(user.getId()) == null) {
            String msg = "User With This Id Does Not Exist";
            log.warn(msg);
            throw new ErrorException(msg);
        } else {
            log.debug("User Updated");
            userStorage.updateUser(user);
        }
    }

    public void addFriend(long userId, long friendId) {
        if (userStorage.findUserById(userId) == null) {
            String msg = "User With This Id Does Not Exist";
            log.warn(msg);
            throw new ErrorException(msg);
        }
        if (userStorage.findUserById(friendId) == null) {
            String msg = "Friend With This Id Does Not Exist";
            log.warn(msg);
            throw new ErrorException(msg);
        }
        userStorage.addFriend(userId,friendId);
    }

//    public void addFriend(long userId, long friendId) {
//        if (inMemoryUserStorage.findUserById(userId).isEmpty()) {
//            String msg = "User With This Id Does Not Exist";
//            log.warn(msg);
//            throw new ErrorException(msg);
//        }
//        if (inMemoryUserStorage.findUserById(friendId).isEmpty()) {
//            String msg = "Friend With This Id Does Not Exist";
//            log.warn(msg);
//            throw new ErrorException(msg);
//        }
//        inMemoryUserStorage.addFriendId(userId, friendId);
//        inMemoryUserStorage.addFriendId(friendId, userId);
//        log.debug("Friend Added");
//    }

//    public List<User> getUsersFriends(long userId) {
//        if (inMemoryUserStorage.findUserById(userId).isEmpty()) {
//            String msg = "User With This Id Does Not Exist";
//            log.warn(msg);
//            throw new ErrorException(msg);
//        } else {
//            return inMemoryUserStorage.findUsersFriends(userId);
//        }
//    }

//    public List<User> getCommonFriends(long userId, long friendId) {
//        if (inMemoryUserStorage.findUserById(userId).isEmpty()) {
//            String msg = "User With This Id Does Not Exist";
//            log.warn(msg);
//            throw new ErrorException(msg);
//        }
//        if (inMemoryUserStorage.findUserById(friendId).isEmpty()) {
//            String msg = "Friend With This Id Does Not Exist";
//            log.warn(msg);
//            throw new ErrorException(msg);
//        }
//        log.debug("Get Common Friends");
//        return inMemoryUserStorage.findCommonFriends(userId, friendId);
//    }

//    public void deleteFriend(long userId, long friendId) {
//        if (inMemoryUserStorage.findUserById(userId).isEmpty()) {
//            String msg = "User With This Id Does Not Exist";
//            log.warn(msg);
//            throw new ErrorException(msg);
//        }
//        if (inMemoryUserStorage.findUserById(friendId).isEmpty()) {
//            String msg = "Friend With This Id Does Not Exist";
//            log.warn(msg);
//            throw new ErrorException(msg);
//        }
//        if (!inMemoryUserStorage.isUsersFriend(userId, friendId)) {
//            String msg = "Not Found Friend In FriendList";
//            log.warn(msg);
//            throw new ErrorException(msg);
//        }
//        inMemoryUserStorage.deleteFriendId(userId, friendId);
//        inMemoryUserStorage.deleteFriendId(friendId, userId);
//        log.debug("Friend Deleted");
//    }
}