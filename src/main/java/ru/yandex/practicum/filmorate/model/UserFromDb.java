package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@Entity
public class UserFromDb {
    @NotNull
    @Id
    private long id;

    private String name;

    @Pattern(regexp = "\\S+", message = "Login Contains Whitespace")
    private String login;

    @Email(message = "Wrong User Email Format")
    private String email;

    @Past(message = "Wrong user birthday")
    private Date birthday;

    private String status;
}
