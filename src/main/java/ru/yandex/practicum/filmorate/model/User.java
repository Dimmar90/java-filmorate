package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    @NotNull
    private long id;

    @Email(message = "Wrong User Email Format")
    private String email;

    private String name;

    @Pattern(regexp = "\\S+", message = "Login contains whitespace")
    private String login;

    @Past(message = "Wrong user birthday")
    private LocalDate birthday;
}