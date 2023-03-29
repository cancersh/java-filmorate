package ru.yandex.practicum.filmorate.controllers;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @ResponseBody
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен POST-запрос на добавление фильма");
        validateFilm(film);
        return filmStorage.create(film);
    }

    @ResponseBody
    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Получен PUT-запрос на обновление фильма с ID={}", film.getId());
        validateFilm(film);
        return filmStorage.update(film);
    }

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Получен GET-запрос на вывод всех фильмов");
        return filmStorage.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") long filmId) {
        log.info("Получен GET-запрос на вывод фильма с ID={}", filmId);
        return filmStorage.getFilmById(filmId);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") long filmId, @PathVariable long userId) {
        log.info("Получен PUT-запрос на добавление лайка");
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") long filmId, @PathVariable long userId) {
        log.info("Получен DELETE-запрос на удаление лайка");
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(name = "count", defaultValue = "10") int count) {
        log.info("Получен GET-запрос на вывод популярных фильмов");
        return filmService.getPopularFilms(count);
    }

    @DeleteMapping("/{id}")
    public Film delete(@PathVariable long filmId) {
        log.info("Получен DELETE-запрос на удаление фильма с ID={}", filmId);
        return filmStorage.delete(filmId);
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Получено исключение: Название фильма не может быть пустым.");
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Получено исключение: Максимальная длина описания — 200 символов.");
            throw new ValidationException("Максимальная длина описания — 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Получено исключение: Дата релиза фильма не может быть раньше 28 декабря 1895 года.");
            throw new ValidationException("Дата релиза фильма не может быть раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() <= 0) {
            log.warn("Получено исключение: Продолжительность фильма должна быть положительной.");
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
        if (film.getId() < 0) {
            log.warn("Получено исключение: id={} не может быть отрицательным.", film.getId());
            throw new ValidationException("Id не может быть отрицательным.");
        }
    }
}
