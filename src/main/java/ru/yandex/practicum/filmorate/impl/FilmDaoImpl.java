package ru.yandex.practicum.filmorate.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Optional;

@Component
public class FilmDaoImpl implements FilmDao {
    private JdbcTemplate jdbcTemplate;

    public FilmDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Film> findLastFilm() {
        return Optional.ofNullable(jdbcTemplate.query("SELECT * FROM FILMS ORDER BY id DESC LIMIT 1",
                new BeanPropertyRowMapper<>(Film.class)).stream().findAny().orElse(null));
    }

    @Override
    public void setFilmId(Film film) {
        if (findLastFilm().isEmpty()) {
            film.setId(1);
        } else {
            film.setId(findLastFilm().get().getId() + 1);
        }
    }

    @Override
    public void addFilm(Film film) {
        jdbcTemplate.update("INSERT INTO FILMS VALUES(?,?,?,?,?,?,?)",
                film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getRate());
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("INSERT INTO FILM_GENRES VALUES(?,?)", film.getId(), genre.getId());
            }
        }
    }

    @Override
    public Optional<Film> findFilmById(long filmID) {
        Film film = new Film();
        String sqlForFilm = "SELECT * FROM FILMS f WHERE f.ID =?";
        String sqlForNpa = "SELECT m.MPA_ID, m.MPA \n" +
                "FROM FILMS f LEFT JOIN MPA m ON f.MPA_ID = m.MPA_ID \n" +
                "WHERE f.ID = ?";
        String sqlForGenres = "SELECT DISTINCT FG.GENRE_ID, g.GENRE_NAME  \n" +
                "FROM FILM_GENRES fg LEFT JOIN GENRE g ON FG .GENRE_ID = g.GENRE_ID \n" +
                "WHERE FG.FILM_ID =?";

        film = jdbcTemplate.query(sqlForFilm, new Object[]{filmID},
                new BeanPropertyRowMapper<>(Film.class)).stream().findAny().orElse(null);
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet(sqlForNpa, filmID);
        if (mpaRow.first()) {
            film.getMpa().setId(mpaRow.getInt("MPA_ID"));
            film.getMpa().setName(mpaRow.getString("MPA"));
        }
        SqlRowSet genreRow = jdbcTemplate.queryForRowSet(sqlForGenres, filmID);
        while (genreRow.next()) {
            Genre genre = new Genre();
            genre.setId(genreRow.getInt("GENRE_ID"));
            genre.setName(genreRow.getString("GENRE_NAME"));
            film.getGenres().add(genre);
        }
        return Optional.ofNullable(film);
    }

    @Override
    public void updateFilm(Film film) {
        jdbcTemplate.update("DELETE FROM FILMS WHERE ID = ?", film.getId());
        jdbcTemplate.update("DELETE FROM FILM_GENRES WHERE  FILM_GENRES.FILM_ID = ?", film.getId());
        addFilm(film);
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

//    @Override
//    public List<Film> findPopularFilms(long listSize) {
//        List<Film> popularFilms = new ArrayList<>();
//        String sqlForIds = "SELECT f.ID FROM FILMS f ORDER BY f.RATE DESC LIMIT ?";
//        SqlRowSet idsRow = jdbcTemplate.queryForRowSet(sqlForIds, listSize);
//        while (idsRow.next()) {
//            popularFilms.add(findFilmById(idsRow.getInt("ID")).get());
//        }
//        return popularFilms;
//    }
}
