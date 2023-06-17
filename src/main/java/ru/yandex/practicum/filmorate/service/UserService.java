package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ErrorException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        inMemoryUserStorage.addUser(user);
        log.debug("User Added");
    }

    public void updateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (inMemoryUserStorage.findUserById(user.getId()).isEmpty()) {
            String msg = "User With This Id Does Not Exist";
            log.warn(msg);
            throw new ErrorException(msg);
        } else {
            inMemoryUserStorage.updateUser(user);
            log.debug("User Updated");
        }
    }

    public List<User> getAllUsers() {
        log.debug("Get All Users");
        return inMemoryUserStorage.findAllUsers();
    }

    public Optional<User> getUserById(long userId) {
        if (inMemoryUserStorage.findUserById(userId).isEmpty()) {
            String msg = "User With This Id Does Not Exist";
            log.warn(msg);
            throw new ErrorException(msg);
        }
        log.debug("Get User By Id");
        return inMemoryUserStorage.findUserById(userId);
    }

    public void addFriend(long userId, long friendId) {
        if (inMemoryUserStorage.findUserById(userId).isEmpty()) {
            String msg = "User With This Id Does Not Exist";
            log.warn(msg);
            throw new ErrorException(msg);
        }
        if (inMemoryUserStorage.findUserById(friendId).isEmpty()) {
            String msg = "Friend With This Id Does Not Exist";
            log.warn(msg);
            throw new ErrorException(msg);
        }
        inMemoryUserStorage.addFriendId(userId, friendId);
        inMemoryUserStorage.addFriendId(friendId, userId);
        log.debug("Friend Added");
    }

    public List<User> getUsersFriends(long userId) {
        if (inMemoryUserStorage.findUserById(userId).isEmpty()) {
            String msg = "User With This Id Does Not Exist";
            log.warn(msg);
            throw new ErrorException(msg);
        } else {
            return inMemoryUserStorage.findUsersFriends(userId);
        }
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        if (inMemoryUserStorage.findUserById(userId).isEmpty()) {
            String msg = "User With This Id Does Not Exist";
            log.warn(msg);
            throw new ErrorException(msg);
        }
        if (inMemoryUserStorage.findUserById(friendId).isEmpty()) {
            String msg = "Friend With This Id Does Not Exist";
            log.warn(msg);
            throw new ErrorException(msg);
        }
        log.debug("Get Common Friends");
        return inMemoryUserStorage.findCommonFriends(userId, friendId);
    }

    public void deleteFriend(long userId, long friendId) {
        if (inMemoryUserStorage.findUserById(userId).isEmpty()) {
            String msg = "User With This Id Does Not Exist";
            log.warn(msg);
            throw new ErrorException(msg);
        }
        if (inMemoryUserStorage.findUserById(friendId).isEmpty()) {
            String msg = "Friend With This Id Does Not Exist";
            log.warn(msg);
            throw new ErrorException(msg);
        }
        if (!inMemoryUserStorage.isUsersFriend(userId, friendId)) {
            String msg = "Not Found Friend In FriendList";
            log.warn(msg);
            throw new ErrorException(msg);
        }
        inMemoryUserStorage.deleteFriendId(userId, friendId);
        inMemoryUserStorage.deleteFriendId(friendId, userId);
        log.debug("Friend Deleted");
    }
}