package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFromDb;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Set<Long>> usersFriendsIDS = new HashMap<>();
    private UserDao userDao;

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
    public UserFromDb findUserById(Long userId) {
        return userDao.findUserById(userId);
    }

    @Override
    public List<UserFromDb> findAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    @Override
    public void addFriend(Long userID, Long friendID) {
//        if (!usersFriendsIDS.containsKey(userID)) {
//            Set<Long> friendsIDs = new HashSet<>();
//            friendsIDs.add(friendID);
//            usersFriendsIDS.put(userID, friendsIDs);
//        } else {
//            usersFriendsIDS.get(userID).add(friendID);
//        }
        userDao.addFriend(userID, friendID);
    }


//    @Override
//    public void addFriendId(Long userID, Long friendID) {
//        if (!usersFriendsIDS.containsKey(userID)) {
//            Set<Long> friendsIDs = new HashSet<>();
//            friendsIDs.add(friendID);
//            usersFriendsIDS.put(userID, friendsIDs);
//        } else {
//            usersFriendsIDS.get(userID).add(friendID);
//        }
//    }

//    @Override
//    public List<User> findUsersFriends(Long userId) {
//        List<User> usersFriends = new ArrayList<>();
//        for (Long friendId : usersFriendsIDS.get(userId)) {
//            usersFriends.add(users.get(friendId));
//        }
//        return usersFriends;
//    }

//    @Override
//    public List<User> findCommonFriends(Long userId, Long friendId) {
//        List<User> commonFriends = new ArrayList<>();
//        try {
//            for (Long userFriendId : usersFriendsIDS.get(userId)) {
//                if (usersFriendsIDS.get(friendId).contains(userFriendId)) {
//                    commonFriends.add(users.get(userFriendId));
//                }
//            }
//            return commonFriends;
//        } catch (NullPointerException e) {
//            return commonFriends;
//        }
//    }

//    @Override
//    public void deleteFriendId(Long userID, Long friendID) {
//        usersFriendsIDS.get(userID).remove(friendID);
//    }

//    @Override
//    public boolean isUsersFriend(Long userId, Long friendId) {
//        boolean isUsersFriend;
//        isUsersFriend = usersFriendsIDS.get(userId).contains(friendId);
//        return isUsersFriend;
//    }
}