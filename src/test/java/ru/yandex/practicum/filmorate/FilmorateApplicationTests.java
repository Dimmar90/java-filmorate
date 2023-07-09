package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
class FilmorateApplicationTests {

    @Autowired
    private UserStorage userStorage;
    @Autowired
    private FilmStorage filmStorage;

    @BeforeEach
    public void createUsers() {
        User fistUser = new User();
        fistUser.setName("FirstUserName");
        fistUser.setLogin("FirstUserLogin");
        fistUser.setEmail("mail@mail.ru");
        fistUser.setBirthday(LocalDate.of(1990, 05, 06));
        userStorage.addUser(fistUser);
        User secondUser = new User();
        secondUser.setName("SecondUserName");
        secondUser.setLogin("SecondUserLogin");
        secondUser.setEmail("mail@mail.ru");
        secondUser.setBirthday(LocalDate.of(1981, 03, 05));
        userStorage.addUser(secondUser);
        User thirdUser = new User();
        thirdUser.setName("ThirdUserName");
        thirdUser.setLogin("ThirdUserLogin");
        thirdUser.setEmail("mail@mail.ru");
        thirdUser.setBirthday(LocalDate.of(1995, 03, 05));
        userStorage.addUser(thirdUser);
    }

    @BeforeEach
    public void createFilm() {
        Mpa mpa = new Mpa();
        mpa.setId(1);
        Genre genre = new Genre();
        genre.setId(1);
        Set<Genre> genres = new LinkedHashSet<>();
        genres.add(genre);

        Film film = new Film();
        film.setName("Film");
        film.setReleaseDate(LocalDate.of(1999, 04, 30));
        film.setDescription("FilmDescription");
        film.setMpa(mpa);
        film.setGenres(genres);
        filmStorage.addFilm(film);

        Film filmSecond = new Film();
        filmSecond.setName("SecondFilm");
        filmSecond.setReleaseDate(LocalDate.of(1999, 04, 30));
        filmSecond.setDescription("FilmDescription");
        filmSecond.setMpa(mpa);
        filmSecond.setGenres(genres);
        filmStorage.addFilm(filmSecond);
    }

    @Test
    public void testFindUserById() {
        User user = userStorage.findUserById(1l);

        assertThat(user).hasFieldOrPropertyWithValue("id", 1l);
    }

    @Test
    public void testFindAllUsers() {
        List<User> listOfAllUsers = userStorage.findAllUsers();

        assertThat(listOfAllUsers.get(0)).hasFieldOrPropertyWithValue("id", 1l);
        assertThat(listOfAllUsers.get(1)).hasFieldOrPropertyWithValue("id", 2l);
        assertThat(listOfAllUsers.get(2)).hasFieldOrPropertyWithValue("id", 3l);
    }

    @Test
    public void testUpdateUser() {
        User updatedUser = new User();
        updatedUser.setId(1l);
        updatedUser.setName("UpdatedUser");
        updatedUser.setLogin("UserLogin");
        updatedUser.setEmail("mail@mail.ru");
        updatedUser.setBirthday(LocalDate.of(1990, 05, 06));

        userStorage.updateUser(updatedUser);
        User userUpdated = userStorage.findUserById(1l);

        assertThat(userUpdated).hasFieldOrPropertyWithValue("name", "UpdatedUser");
    }

    @Test
    public void testFindUsersFriends() {
        userStorage.addFriend(1l, 2l);

        List<User> usersFriend = userStorage.findUsersFriends(1l);

        assertThat(usersFriend.get(0)).hasFieldOrPropertyWithValue("id", 2l);
    }

    @Test
    public void testFindCommonFriends() {
        userStorage.addFriend(1l, 3l);
        userStorage.addFriend(2l, 3l);

        List<User> usersCommonFriends = userStorage.findCommonFriends(1l, 2l);

        assertThat(usersCommonFriends.get(0)).hasFieldOrPropertyWithValue("id", 3l);
    }

    @Test
    public void testDeleteFriendId() {
        userStorage.addFriend(1l, 2l);
        List<User> usersFriend = userStorage.findUsersFriends(1l);

        assertThat(usersFriend.get(0)).hasFieldOrPropertyWithValue("id", 2l);

        userStorage.deleteFriendId(1l, 2l);
        usersFriend = userStorage.findUsersFriends(1l);

        assertThat(usersFriend).isEmpty();
    }

    @Test
    public void testFindFilmById() {
        Film film = filmStorage.findFilmById(1l);

        assertThat(film).hasFieldOrPropertyWithValue("id", 1l);
    }

    @Test
    public void testUpdateFilm() {
        Mpa mpa = new Mpa();
        mpa.setId(2);
        Genre genre = new Genre();
        genre.setId(2);
        Set<Genre> genres = new LinkedHashSet<>();
        genres.add(genre);

        Film film = new Film();
        film.setId(1l);
        film.setName("FilmUpdated");
        film.setReleaseDate(LocalDate.of(1999, 04, 30));
        film.setDescription("FilmDescription");
        film.setMpa(mpa);
        film.setGenres(genres);
        filmStorage.updateFilm(film);

        Film updatedFilm = filmStorage.findFilmById(1l);

        assertThat(updatedFilm).hasFieldOrPropertyWithValue("name", "FilmUpdated");
    }

    @Test
    public void testFindAllFilms() {
        List<Film> listOfFilms = filmStorage.findAllFilms();

        assertThat(listOfFilms.get(0)).hasFieldOrPropertyWithValue("id", 1l);
    }

    @Test
    public void testAddLike() {
        filmStorage.addLike(1l);

        Film film = filmStorage.findFilmById(1l);

        assertThat(film).hasFieldOrPropertyWithValue("rate", 1l);
    }

    @Test
    public void testDeleteLike() {
        filmStorage.addLike(1l);

        Film film = filmStorage.findFilmById(1l);

        assertThat(film).hasFieldOrPropertyWithValue("rate", 1l);

        filmStorage.deleteLike(1l);

        film = filmStorage.findFilmById(1l);

        assertThat(film).hasFieldOrPropertyWithValue("rate", 0l);
    }

    @Test
    public void testFindMostPopularFilms() {
        filmStorage.addLike(2l);

        List<Film> listOfMostPopularFilms = filmStorage.findMostPopularFilms(2);

        assertThat(listOfMostPopularFilms.get(0)).hasFieldOrPropertyWithValue("id", 2l);
    }

    @Test
    public void testFindAllMpa() {
        List<Mpa> listOfMpa = filmStorage.findAllMpa();
        assertThat(listOfMpa.stream().findFirst().get()).hasFieldOrPropertyWithValue("name", "G");
        assertThat(listOfMpa.get(listOfMpa.size() - 1)).hasFieldOrPropertyWithValue("name", "NC-17");
    }

    @Test
    public void testFindMpaById() {
        Mpa mpa = filmStorage.findMpaById(2);

        assertThat(mpa).hasFieldOrPropertyWithValue("name", "PG");
    }

    @Test
    public void testFindAllGenres() {
        List<Genre> listOfGenres = filmStorage.findAllGenres();

        assertThat(listOfGenres.stream().findFirst().get()).hasFieldOrPropertyWithValue("name", "Комедия");
        assertThat(listOfGenres.get(listOfGenres.size() - 1)).hasFieldOrPropertyWithValue("name", "Боевик");
    }

    @Test
    public void testFindGenreById() {
        Genre genre = filmStorage.findGenreById(3);

        assertThat(genre).hasFieldOrPropertyWithValue("name", "Мультфильм");
    }
}