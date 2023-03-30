package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;


@Data
public class Film {
    private int id;

//    @NotBlank(message = "Film Name is blank")
//    @NotNull(message = "Film name is absent")
    private String name;

//    @Size(min = 1, max = 200)
    private String description;

    private LocalDate releaseDate;

//    @Positive(message = "Film duration is negative value")
    private int duration;
}








