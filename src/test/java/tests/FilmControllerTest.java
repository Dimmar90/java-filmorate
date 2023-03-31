package tests;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.time.Month;

public class FilmControllerTest {
    private final FilmController filmController = new FilmController();
    @Rule
    public final LoggerRule loggerRule = new LoggerRule();

    public Film filmAccount() {
        Film film = new Film();
        film.setName("filmNameTest");
        film.setDescription("filmDescription");
        film.setDuration(120);
        film.setReleaseDate(LocalDate.parse("1990-05-06"));
        return film;
    }

    @Test
    public void createFilmTest() {
        Film film = filmAccount();
        String expectingLoggerMessage = "Film added: " + film.getName();

        filmController.create(film);

        Assert.assertEquals(expectingLoggerMessage, loggerRule.getFormattedMessages().get(0));
    }

    @Test
    public void wrongFilmName() {
        Film film = filmAccount();
        film.setName("");
        String expectingLoggerMessage = "Empty film name";

        filmController.create(film);

        Assert.assertEquals(expectingLoggerMessage, loggerRule.getFormattedMessages().get(0));
    }

    @Test
    public void wrongFilmDescription() {
        Film film = filmAccount();
        String generatedDescription = RandomStringUtils.randomAlphabetic(201);
        film.setDescription(generatedDescription);
        String expectingLoggerMessage = "Description film limit";

        filmController.create(film);

        Assert.assertEquals(expectingLoggerMessage, loggerRule.getFormattedMessages().get(0));
    }

    @Test
    public void wrongFilmDuration() {
        Film film = filmAccount();
        film.setDuration(-1);
        String expectingLoggerMessage = "Wrong duration of film";

        filmController.create(film);

        Assert.assertEquals(expectingLoggerMessage, loggerRule.getFormattedMessages().get(0));
    }

    @Test
    public void wrongFilmReleaseDate(){
        Film film = filmAccount();
        film.setReleaseDate(LocalDate.of(1895, Month.DECEMBER, 27));
        String expectingLoggerMessage = "Wrong release date of film";

        filmController.create(film);

        Assert.assertEquals(expectingLoggerMessage, loggerRule.getFormattedMessages().get(0));
    }
}
