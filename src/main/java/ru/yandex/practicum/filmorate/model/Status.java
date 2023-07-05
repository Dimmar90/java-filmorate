package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Status {
    @Id
    private Integer status_Id;

    private String status;

//    public Status(Integer status_Id, String status) {
//        this.status_Id = status_Id;
//        this.status = status;
//    }

    public Status() {
    }
}
