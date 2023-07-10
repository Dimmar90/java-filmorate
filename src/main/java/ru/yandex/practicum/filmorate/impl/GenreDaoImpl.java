package ru.yandex.practicum.filmorate.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;

@Component
public class GenreDaoImpl implements GenreDao {
    private JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> findAllGenres() {
        List<Genre> genres = new ArrayList<>();
        String sqlGetAllGenres = "SELECT * FROM GENRE g";
        SqlRowSet genresRow = jdbcTemplate.queryForRowSet(sqlGetAllGenres);
        while (genresRow.next()) {
            Genre genre = new Genre();
            genre.setId(genresRow.getInt("GENRE_ID"));
            genre.setName(genresRow.getString("GENRE_NAME"));
            genres.add(genre);
        }
        return genres;
    }

    @Override
    public Genre findGenreById(int genreId) {
        Genre genre = new Genre();
        String sqlGetGenreById = "SELECT * FROM GENRE g WHERE g.GENRE_ID =?";
        SqlRowSet genreRow = jdbcTemplate.queryForRowSet(sqlGetGenreById, genreId);
        if (genreRow.first()) {
            genre.setId(genreRow.getInt("GENRE_ID"));
            genre.setName(genreRow.getString("GENRE_NAME"));
        } else {
            return null;
        }
        return genre;
    }
}