package ru.yandex.practicum.filmorate.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public Optional<User> findLastUser() {
        return Optional.ofNullable(jdbcTemplate.query("SELECT * FROM USERS ORDER BY id DESC LIMIT 1",
                new BeanPropertyRowMapper<>(User.class)).stream().findAny().orElse(null));
    }

    @Override
    public void setUsersId(User user) {
        if (findLastUser().isEmpty()) {
            user.setId(1);
        } else {
            user.setId(findLastUser().get().getId() + 1);
        }
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
    public Optional<User> findUserById(Long id) {
        return Optional.ofNullable(jdbcTemplate.query("SELECT * FROM USERS AS u WHERE ID = ?",
                new Object[]{id}, new BeanPropertyRowMapper<>(User.class)).stream().findAny().orElse(null));
    }

    @Override
    public List<User> findAllUsers() {
        return jdbcTemplate.query("SELECT * FROM USERS ",
                new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        Integer str = null;
        Integer status = null;
        String sql = "SELECT *\n" +
                "FROM FRIENDS f \n" +
                "WHERE f.ID_USER=? AND f.ID_FRIEND =?";
        SqlRowSet isExistRow = jdbcTemplate.queryForRowSet(sql, userId, friendId);
        if (isExistRow.first()) {
            str = isExistRow.getInt("ID_USER");
        }
        if (str == null) {
            jdbcTemplate.update("INSERT INTO FRIENDS VALUES (?,?,?)", userId, friendId, 2);
        }

        SqlRowSet statusRow = jdbcTemplate.queryForRowSet(sql, friendId, userId);
        if (statusRow.first()) {
            status = statusRow.getInt("STATUS_ID");
        }

        if (status == null) {
            return;
        } else if (status == 2) {
            jdbcTemplate.update("UPDATE FRIENDS \n" +
                    "SET STATUS_ID =1\n" +
                    "WHERE ID_USER =? AND ID_FRIEND =?", friendId, userId);

            jdbcTemplate.update("UPDATE FRIENDS \n" +
                    "SET STATUS_ID =1\n" +
                    "WHERE ID_USER =? AND ID_FRIEND =?", userId, friendId);
        }
    }

    @Override
    public List<User> findUserFriends(Long userId) {
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
                new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public List<User> findCommonFriends(Long firstUserId, Long secondUserId) {
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
            commonFriends.add(findUserById(id).get());
        }
        return commonFriends;
    }

    @Override
    public void deleteFriend(Long userID, Long friendID) {
        String sql = "DELETE \n" +
                "FROM FRIENDS  \n" +
                "WHERE FRIENDS.ID_USER =? AND FRIENDS.ID_FRIEND =?";
        jdbcTemplate.update(sql, userID, friendID);
        jdbcTemplate.update(sql, friendID, userID);
    }
}