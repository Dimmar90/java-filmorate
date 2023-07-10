package ru.yandex.practicum.filmorate.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.List;

@Component
public class MpaDaoImpl implements MpaDao {

    private JdbcTemplate jdbcTemplate;

    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> findAllMpa() {
        List<Mpa> mpaList = new ArrayList<>();
        String sqlMpa = "SELECT * FROM MPA m ";
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet(sqlMpa);
        while (mpaRow.next()) {
            Mpa mpa = new Mpa();
            mpa.setId(mpaRow.getInt("MPA_ID"));
            mpa.setName(mpaRow.getString("MPA"));
            mpaList.add(mpa);
        }

        return mpaList;
    }

    @Override
    public Mpa findMpaById(Integer mpaId) {
        Mpa mpa = new Mpa();
        String sqlGetMpaByID = "SELECT * FROM MPA m WHERE m.MPA_ID =?";
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet(sqlGetMpaByID, mpaId);
        if (mpaRow.first()) {
            mpa.setId(mpaRow.getInt("MPA_ID"));
            mpa.setName(mpaRow.getString("MPA"));
        } else {
            return null;
        }
        return mpa;
    }
}
