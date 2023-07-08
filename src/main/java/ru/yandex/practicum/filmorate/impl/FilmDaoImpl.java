package ru.yandex.practicum.filmorate.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmWithGenres;

import java.util.ArrayList;
import java.util.List;

@Component
public class FilmDaoImpl implements FilmDao {
    private JdbcTemplate jdbcTemplate;

    public FilmDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film findLastFilm() {
        return jdbcTemplate.query("SELECT * FROM FILMS ORDER BY id DESC LIMIT 1",
                new BeanPropertyRowMapper<>(Film.class)).stream().findAny().orElse(null);
    }

    @Override
    public void addFilm(Film film) {
        jdbcTemplate.update("INSERT INTO FILMS VALUES(?,?,?,?,?,?,?)",
                film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getRate());
        if (film.getGenres() != null) {
            for (Integer genreId : film.getGenres()) {
                jdbcTemplate.update("INSERT INTO FILM_GENRES VALUES(?,?)", film.getId(), genreId);
            }
        }
    }

    @Override
    public FilmWithGenres findFilmById(long filmID) {
        FilmWithGenres film = new FilmWithGenres();
        List<String> listOfGenres = new ArrayList<>();
        String sqlForFilmWithMpa = "SELECT f.ID ,f.NAME ,f.DESCRIPTION ,f.RELEASE_DATE ,f.DURATION ,m.MPA, f.RATE  \n" +
                "FROM FILMS f LEFT JOIN MPA m ON f.MPA_ID = m.MPA_ID \n" +
                "WHERE f.ID =?";
        String sqlForGenres = "SELECT DISTINCT g.GENRE_NAME \n" +
                "FROM FILM_GENRES fg LEFT JOIN GENRE g ON fg.GENRE_ID = g.GENRE_ID \n" +
                "WHERE fg.FILM_ID =?";

        film = jdbcTemplate.query(sqlForFilmWithMpa, new Object[]{filmID},
                new BeanPropertyRowMapper<>(FilmWithGenres.class)).stream().findAny().orElse(null);

        SqlRowSet genresRow = jdbcTemplate.queryForRowSet(sqlForGenres, filmID);
        while (genresRow.next()) {
            String genreName = genresRow.getString("GENRE_NAME");
            listOfGenres.add(genreName);
        }

        film.setGenres(listOfGenres);
        return film;
    }

    @Override
    public void updateFilm(Film film) {
        jdbcTemplate.update("DELETE FROM FILMS WHERE ID = ?", film.getId());
        jdbcTemplate.update("DELETE FROM FILM_GENRES WHERE FILM_ID = ?", film.getId());
        addFilm(film);
    }

    @Override
    public List<FilmWithGenres> getAllFilms() {
        List<FilmWithGenres> filmsList = new ArrayList<>();
        String sqlFilmWithMpa = "SELECT f.ID ,f.NAME ,f.DESCRIPTION ,f.RELEASE_DATE ,f.DURATION ,m.MPA, f.RATE   \n" +
                "FROM FILMS f LEFT JOIN MPA m ON f.MPA_ID = m.MPA_ID ";
        String sqlForGenres = "SELECT DISTINCT g.GENRE_NAME \n" +
                "FROM FILM_GENRES fg LEFT JOIN GENRE g ON fg.GENRE_ID = g.GENRE_ID \n" +
                "WHERE fg.FILM_ID =?";

        filmsList = jdbcTemplate.query(sqlFilmWithMpa, new BeanPropertyRowMapper<>(FilmWithGenres.class));
        for (FilmWithGenres film : filmsList) {
            List<String> listOfGenres = new ArrayList<>();
            SqlRowSet genresRow = jdbcTemplate.queryForRowSet(sqlForGenres, film.getId());
            while (genresRow.next()) {
                String genreName = genresRow.getString("GENRE_NAME");
                listOfGenres.add(genreName);
            }
            film.setGenres(listOfGenres);
        }
        return filmsList;
    }

    @Override
    public void addLike(Long filmId) {
        String sql = "UPDATE FILMS SET FILMS.RATE = FILMS.RATE +1 WHERE ID=?";
        jdbcTemplate.update(sql, filmId);
    }

    @Override
    public void deleteLike(long filmId) {
        String sql = "UPDATE FILMS SET FILMS.RATE = FILMS.RATE -1 WHERE ID=?";
        jdbcTemplate.update(sql, filmId);
    }

    @Override
    public List<Film> getPopularFilms(long listSize) {
        String sql = "SELECT * FROM FILMS f ORDER BY f.RATE DESC LIMIT ?";
        return jdbcTemplate.query(sql, new Object[]{listSize}, new BeanPropertyRowMapper<>(Film.class));
    }
}
