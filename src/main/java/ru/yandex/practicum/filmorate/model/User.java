package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
public class User {
    private int id;

    private String email;

    @NotEmpty ()
    @NotBlank ()
    private String login;

    private String name;

    private LocalDate birthday;
}