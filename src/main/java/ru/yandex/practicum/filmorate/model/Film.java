package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
public class Film {
    @NotNull
    private long id;
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    private int duration;
    private Date release_Date;
    private int mpaId;
    private long rate;
    private List<Integer> genres;
}