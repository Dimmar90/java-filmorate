package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    public Map<Long, User> getUsers() {
        return users;
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
    public void deleteFriendId(Long userID, Long friendID) {
        usersFriendsIDS.get(userID).remove(friendID);
    }

    @Override
    public Map<Long, Set<Long>> getUsersFriendsIds() {
        return usersFriendsIDS;
    }
}