package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class Film {
    @NotNull
    private long id;
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    private int duration;
    private LocalDate releaseDate;
    private Mpa mpa = new Mpa();
    private long rate;
    private Set<Genre> genres = new LinkedHashSet<>();
}