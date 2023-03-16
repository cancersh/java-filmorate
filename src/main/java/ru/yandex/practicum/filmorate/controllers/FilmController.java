package ru.yandex.practicum.filmorate.controllers;

import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final List<Film> films = new ArrayList<>();

    @ResponseBody
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен POST-запрос на добавление фильма");
        validateFilm(film);
        films.add(film);
        return film;
    }

    @ResponseBody
    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Получен PUT-запрос на обновление фильма с ID={}", film.getId());
        validateFilm(film);
        films.set(film.getId(), film);
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        return films;
    }

    public void validateFilm(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank()) {
            log.info("Получено исключение: Название фильма не может быть пустым.");
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            log.info("Получено исключение: Максимальная длина описания — 200 символов.");
            throw new ValidationException("Максимальная длина описания — 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Получено исключение: Дата релиза фильма не может быть раньше 28 декабря 1895 года.");
            throw new ValidationException("Дата релиза фильма не может быть раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() <= 0) {
            log.info("Получено исключение: Продолжительность фильма должна быть положительной.");
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
        if (film.getId() < 0) {
            log.info("Получено исключение: id не может быть отрицательным.");
            throw new ValidationException("Id не может быть отрицательным.");
        }
    }
}