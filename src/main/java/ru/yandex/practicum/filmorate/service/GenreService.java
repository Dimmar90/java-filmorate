package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.WrongIdException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
public class GenreService {
    private GenreDao genreDao;
    private final Logger log = LoggerFactory.getLogger(FilmService.class);

    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public List<Genre> getAllGenres() {
        return genreDao.findAllGenres();
    }

    public Genre getGenreById(int genreId) {
        if (genreDao.findGenreById(genreId) == null) {
            String msg = "Wrong Genre Id";
            log.warn(msg);
            throw new WrongIdException(msg);
        } else {
            return genreDao.findGenreById(genreId);
        }
    }
}
