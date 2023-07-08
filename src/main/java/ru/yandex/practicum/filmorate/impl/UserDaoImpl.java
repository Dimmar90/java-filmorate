package ru.yandex.practicum.filmorate.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserWithStatus;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User findLastUser() {
        return jdbcTemplate.query("SELECT * FROM USERS ORDER BY id DESC LIMIT 1",
                new BeanPropertyRowMapper<>(User.class)).stream().findAny().orElse(null);
    }

    @Override
    public void addUser(User user) {
        jdbcTemplate.update("INSERT INTO USERS VALUES(?,?,?,?,?)",
                user.getId(), user.getName(), user.getLogin(), user.getEmail(), user.getBirthday());
    }

    @Override
    public void updateUser(User user) {
        jdbcTemplate.update("DELETE FROM USERS WHERE id = ?", user.getId());
        addUser(user);
    }

    @Override
    public User findUserById(Long id) {
        return jdbcTemplate.query("SELECT * FROM USERS AS u WHERE ID = ?",
                new Object[]{id}, new BeanPropertyRowMapper<>(User.class)).stream().findAny().orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM USERS ",
                new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        String request = null;
        Integer statusId = null;
        String sqlForRequest = "SELECT REQUEST_FOR_FRIENDSHIP FROM FRIENDS f WHERE ID_USER =? AND ID_FRIEND=?";
        String sqlForStatusId = "SELECT STATUS_ID  FROM FRIENDS f WHERE ID_USER =? AND ID_FRIEND=?";
        SqlRowSet requestRow = jdbcTemplate.queryForRowSet(sqlForRequest, userId, friendId);
        SqlRowSet statusRow = jdbcTemplate.queryForRowSet(sqlForStatusId, userId, friendId);
        if (requestRow.first()) {
            request = requestRow.getString("REQUEST_FOR_FRIENDSHIP");
        }
        if (statusRow.first()) {
            statusId = statusRow.getInt("STATUS_ID");
        }
        try {
            if (statusId == 2 && request.equals("added")) {
                jdbcTemplate.update("UPDATE FRIENDS \n" +
                        "SET REQUEST_FOR_FRIENDSHIP = NULL,\n" +
                        "STATUS_ID =1\n" +
                        "WHERE ID_USER =? AND ID_FRIEND=?", userId, friendId);
                jdbcTemplate.update("UPDATE FRIENDS \n" +
                        "SET REQUEST_FOR_FRIENDSHIP = NULL,\n" +
                        "STATUS_ID =1\n" +
                        "WHERE ID_USER =? AND ID_FRIEND=?", friendId, userId);
            }
        } catch (NullPointerException e) {
            jdbcTemplate.update("INSERT INTO FRIENDS VALUES (?,?,?,?)", userId, friendId, null, 2);
            jdbcTemplate.update("INSERT INTO FRIENDS VALUES (?,?,?,?)", friendId, userId, "added", 2);
        }
    }

    @Override
    public List<UserWithStatus> getUserFriends(Long userId) {
        String sql = "SELECT DISTINCT f.ID_FRIEND,\n" +
                "f.ID_USER,\n" +
                "s.STATUS,\n" +
                "u.ID,\n" +
                "u.NAME,\n" +
                "u.LOGIN,\n" +
                "u.EMAIL,\n" +
                "u.BIRTHDAY \n" +
                "FROM FRIENDS f LEFT JOIN USERS u ON f.ID_FRIEND = u.ID LEFT JOIN STATUS s ON f.STATUS_ID = s.STATUS_ID\n" +
                "WHERE f.ID_USER =?";
        return jdbcTemplate.query(sql, new Object[]{userId},
                new BeanPropertyRowMapper<>(UserWithStatus.class));
    }

    @Override
    public List<User> getCommonFriends(Long firstUserId, Long secondUserId) {
        List<User> commonFriends = new ArrayList<>();
        List<Long> commonFriendsIds = new ArrayList<>();
        String findCommonFriendsIds = "SELECT f.ID_FRIEND , COUNT(f.ID_FRIEND) AS cnt \n" +
                "FROM FRIENDS f \n" +
                "WHERE f.ID_USER = ? OR f.ID_USER =?\n" +
                "GROUP BY f.ID_FRIEND \n" +
                "HAVING cnt > 1";
        SqlRowSet idsRow = jdbcTemplate.queryForRowSet(findCommonFriendsIds, firstUserId, secondUserId);
        while (idsRow.next()) {
            Long id = idsRow.getLong("ID_FRIEND");
            commonFriendsIds.add(id);
        }
        for (Long id : commonFriendsIds) {
            commonFriends.add(findUserById(id));
        }
        return commonFriends;
    }

    @Override
    public void deleteFriend(Long userID, Long friendID) {
        String sql = "DELETE \n" +
                "FROM FRIENDS f \n" +
                "WHERE f.ID_USER =? AND f.ID_FRIEND =?";
        jdbcTemplate.update(sql, userID, friendID);
        jdbcTemplate.update(sql, friendID, userID);
    }
}




