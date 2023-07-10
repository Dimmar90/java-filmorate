package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.WrongIdException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
public class MpaService {
    private MpaDao mpaDao;
    private final Logger log = LoggerFactory.getLogger(FilmService.class);

    public MpaService(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public List<Mpa> getAllMpa() {
        return mpaDao.findAllMpa();
    }

    public Mpa getMpaById(int mpaId) {
        if (mpaDao.findMpaById(mpaId) == null) {
            String msg = "Wrong MPA Id";
            log.warn(msg);
            throw new WrongIdException(msg);
        } else {
            return mpaDao.findMpaById(mpaId);
        }
    }
}
