package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Data
public class FilmWithGenres {
    @NotNull
    private long id;
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    private int duration;
    private Date releaseDate;
    private long rate;
    private String mpa;
    private List<String> genres;
}

