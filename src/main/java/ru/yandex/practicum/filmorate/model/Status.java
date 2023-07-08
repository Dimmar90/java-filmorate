package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Status {
    @Id
    private Integer statusId;

    private String status;

    public Status() {
    }
}
