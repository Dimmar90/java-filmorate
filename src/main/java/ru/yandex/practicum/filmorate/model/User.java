package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    @NotNull
    private int id;

    @Email(message = "Wrong user email format")
    private String email;

    @Pattern(regexp = "\\S+", message = "Login contains whitespace")
    private String login;

    private String name;

    @Past(message = "Wrong user birthday")
    private LocalDate birthday;
}