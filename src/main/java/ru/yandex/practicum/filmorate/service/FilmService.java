package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.WrongIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.stream.Collectors.toCollection;

@Service
public class FilmService {
    private FilmDao filmDao;
    private UserDao userDao;
    private JdbcTemplate jdbcTemplate;
    private final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final LocalDate firstFilmRelease = LocalDate.of(1895, 12, 12);


    public FilmService(FilmDao filmDao, UserDao userDao, JdbcTemplate jdbcTemplate) {
        this.filmDao = filmDao;
        this.userDao = userDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addFilm(Film film) {
        filmDurationAndReleaseDateExceptions(film);
        filmDao.setFilmId(film);
        filmDao.addFilm(film);
        log.debug("Film Added: {}", film);
    }

    public Film getFilmById(long filmID) {
        wrongIdFilmException(filmID);
        log.debug("Get Film By Id");
        return filmDao.findFilmById(filmID).get();
    }

    public void updateFilm(Film film) {
        filmDurationAndReleaseDateExceptions(film);
        wrongIdFilmException(film.getId());
        filmDao.updateFilm(film);
        log.debug("Film Updated");
    }

    public List<Film> getAllFilms() {
        HashMap<Long, Film> allFilms = new HashMap<>();
        String sqlGetAllFilms = "SELECT f.ID,f.NAME ,f.DESCRIPTION ,f.RELEASE_DATE ,f.DURATION ,f.RATE ,\n" +
                "m.MPA_ID ,m.MPA ,\n" +
                "g.GENRE_ID ,g.GENRE_NAME\n" +
                "FROM FILMS f LEFT JOIN MPA m ON f.MPA_ID = m.MPA_ID LEFT JOIN FILM_GENRES fg ON " +
                "f.ID = fg.FILM_ID LEFT JOIN GENRE g ON fg.GENRE_ID = g.GENRE_ID ";
        SqlRowSet filmRow = jdbcTemplate.queryForRowSet(sqlGetAllFilms);
        while (filmRow.next()) {
            Film film = new Film();
            film.setId(filmRow.getInt("ID"));
            film.setName(filmRow.getString("NAME"));
            film.setDescription(filmRow.getString("DESCRIPTION"));
            film.setReleaseDate(filmRow.getDate("RELEASE_DATE").toLocalDate());
            film.setDuration(filmRow.getInt("DURATION"));
            film.setRate(filmRow.getInt("RATE"));
            Mpa mpa = new Mpa();
            mpa.setId(filmRow.getInt("MPA_ID"));
            mpa.setName(filmRow.getString("MPA"));
            film.setMpa(mpa);

            if (!allFilms.keySet().contains(film.getId())) {
                allFilms.put(film.getId(), film);
            }
            if (filmRow.getInt("GENRE_ID") == 0) {
                continue;
            }
            Genre genre = new Genre();
            genre.setId(filmRow.getInt("GENRE_ID"));
            genre.setName(filmRow.getString("GENRE_NAME"));
            allFilms.get(film.getId()).getGenres().add(genre);
        }
        return allFilms.values().stream().collect(toCollection(ArrayList::new));
    }

    public void addLike(long filmID, long userId) {
        wrongIdFilmException(filmID);
        wrongIdUserException(userId);
        filmDao.addLike(filmID);
        log.debug("Like Added");
    }

    public void deleteLike(long filmID, long userId) {
        wrongIdFilmException(filmID);
        wrongIdUserException(userId);
        filmDao.deleteLike(filmID);
        log.debug("Like Deleted");
    }

    public List<Film> getPopularFilms(String count) {
        long limit;
        if (count == null) {
            limit = 10;
        } else {
            limit = Long.parseLong(count);
        }
        HashMap<Long, Film> allPopularFilms = new HashMap<>();
        String sqlGetAllPopularFilms = "SELECT f.ID,f.NAME ,f.DESCRIPTION ,f.RELEASE_DATE ,f.DURATION ,f.RATE ,\n" +
                "m.MPA_ID ,m.MPA ,\n" +
                "g.GENRE_ID ,g.GENRE_NAME\n" +
                "FROM FILMS f LEFT JOIN MPA m ON f.MPA_ID = m.MPA_ID LEFT JOIN FILM_GENRES fg ON f.ID = fg.FILM_ID LEFT JOIN GENRE g ON fg.GENRE_ID = g.GENRE_ID \n" +
                "ORDER BY rate DESC \n" +
                "LIMIT ?";
        SqlRowSet filmRow = jdbcTemplate.queryForRowSet(sqlGetAllPopularFilms, limit);
        while (filmRow.next()) {
            Film film = new Film();
            film.setId(filmRow.getInt("ID"));
            film.setName(filmRow.getString("NAME"));
            film.setDescription(filmRow.getString("DESCRIPTION"));
            film.setReleaseDate(filmRow.getDate("RELEASE_DATE").toLocalDate());
            film.setDuration(filmRow.getInt("DURATION"));
            film.setRate(filmRow.getInt("RATE"));
            Mpa mpa = new Mpa();
            mpa.setId(filmRow.getInt("MPA_ID"));
            mpa.setName(filmRow.getString("MPA"));
            film.setMpa(mpa);

            if (!allPopularFilms.keySet().contains(film.getId())) {
                allPopularFilms.put(film.getId(), film);
            }
            if (filmRow.getInt("GENRE_ID") == 0) {
                continue;
            }
            Genre genre = new Genre();
            genre.setId(filmRow.getInt("GENRE_ID"));
            genre.setName(filmRow.getString("GENRE_NAME"));
            allPopularFilms.get(film.getId()).getGenres().add(genre);
        }
        return allPopularFilms.values().stream().collect(toCollection(ArrayList::new));
    }

    public void filmDurationAndReleaseDateExceptions(Film film) {
        if (film.getDuration() <= 0) {
            String msg = "Wrong Duration Of Film";
            log.warn(msg);
            throw new BadRequestException(msg);
        }
        if (film.getReleaseDate().isBefore(firstFilmRelease)) {
            String msg = "Wrong Release Date Of Film";
            log.warn(msg);
            throw new BadRequestException(msg);
        }
    }

    public void wrongIdFilmException(long filmId) {
        if (filmDao.findFilmById(filmId).isEmpty()) {
            String msg = "Wrong Film Id";
            log.warn(msg);
            throw new WrongIdException(msg);
        }
    }

    public void wrongIdUserException(long userId) {
        if (userDao.findUserById(userId).isEmpty()) {
            String msg = "Wrong User Id";
            log.warn(msg);
            throw new WrongIdException(msg);
        }
    }
}