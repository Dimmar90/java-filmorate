package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    private int id;

    @Email
    private String email;

    private String login;

    private String name;

    private LocalDate birthday;
}