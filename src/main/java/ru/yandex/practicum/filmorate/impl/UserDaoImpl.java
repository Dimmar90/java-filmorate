package ru.yandex.practicum.filmorate.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFromDb;

import java.util.List;

@Component
public class UserDaoImpl implements UserDao {
    private JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User findLastUser() {
        return jdbcTemplate.query("SELECT * FROM users ORDER BY id DESC LIMIT 1",
                new BeanPropertyRowMapper<>(User.class)).stream().findAny().orElse(null);
    }

    @Override
    public void addUser(User user) {
        jdbcTemplate.update("INSERT INTO users VALUES(?,?,?,?,?,?)",
                user.getId(), user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getStatus_id());
    }

    @Override
    public void updateUser(User user) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", user.getId());
        addUser(user);
    }

    @Override
    public UserFromDb findUserById(Long id) {
        return jdbcTemplate.query("SELECT * FROM users AS u LEFT JOIN status AS s ON u.status_id = s.status_id WHERE ID = ?",
                new Object[]{id}, new BeanPropertyRowMapper<>(UserFromDb.class)).stream().findAny().orElse(null);
    }

    @Override
    public List<UserFromDb> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM users AS u LEFT JOIN status AS s ON u.status_id = s.status_id",
                new BeanPropertyRowMapper<>(UserFromDb.class));
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        jdbcTemplate.update("insert into friends values(?,?,?) ON CONFLICT DO NOTHING", userId, friendId, 2);
        jdbcTemplate.update("insert into friends values(?,?,?) ON CONFLICT DO NOTHING", friendId, userId, 2);
    }
}
