package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.service.MpaService;

@RestController
public class MpaController {
    private MpaService mpaService;

    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping(value = "/mpa")
    public ResponseEntity<?> getAllMpa() {
        return new ResponseEntity<>(mpaService.getAllMpa(), HttpStatus.OK);
    }

    @GetMapping(value = "/mpa/{id}")
    public ResponseEntity<?> getMpaById(@PathVariable int id) {
        return new ResponseEntity<>(mpaService.getMpaById(id), HttpStatus.OK);
    }
}
