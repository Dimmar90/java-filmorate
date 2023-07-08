package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Set<Long>> usersFriendsIDS = new HashMap<>();
    private long id = 0;

    @Override
    public void addUser(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
    }

    @Override
    public void updateUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public Optional<User> findUserById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void addFriendId(Long userID, Long friendID) {
        if (!usersFriendsIDS.containsKey(userID)) {
            Set<Long> friendsIDs = new HashSet<>();
            friendsIDs.add(friendID);
            usersFriendsIDS.put(userID, friendsIDs);
        } else {
            usersFriendsIDS.get(userID).add(friendID);
        }
    }

    @Override
    public List<User> findUsersFriends(Long userId) {
        List<User> usersFriends = new ArrayList<>();
        for (Long friendId : usersFriendsIDS.get(userId)) {
            usersFriends.add(users.get(friendId));
        }
        return usersFriends;
    }

    @Override
    public List<User> findCommonFriends(Long userId, Long friendId) {
        List<User> commonFriends = new ArrayList<>();
        try {
            for (Long userFriendId : usersFriendsIDS.get(userId)) {
                if (usersFriendsIDS.get(friendId).contains(userFriendId)) {
                    commonFriends.add(users.get(userFriendId));
                }
            }
            return commonFriends;
        } catch (NullPointerException e) {
            return commonFriends;
        }
    }

    @Override
    public void deleteFriendId(Long userID, Long friendID) {
        usersFriendsIDS.get(userID).remove(friendID);
    }

    @Override
    public boolean isUsersFriend(Long userId, Long friendId) {
        boolean isUsersFriend;
        isUsersFriend = usersFriendsIDS.get(userId).contains(friendId);
        return isUsersFriend;
    }
}