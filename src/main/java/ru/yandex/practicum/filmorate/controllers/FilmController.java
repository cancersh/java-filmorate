package ru.yandex.practicum.filmorate.controllers;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(@Qualifier("filmDbStorage") FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @ResponseBody
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен POST-запрос на добавление фильма");
        return filmStorage.create(film);
    }

    @ResponseBody
    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Получен PUT-запрос на обновление фильма с ID={}", film.getId());
        return filmStorage.update(film);
    }

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Получен GET-запрос на вывод всех фильмов");
        return filmStorage.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") Long filmId) {
        log.info("Получен GET-запрос на вывод фильма с ID={}", filmId);
        return filmStorage.getFilmById(filmId);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Long filmId, @PathVariable Long userId) {
        log.info("Получен PUT-запрос на добавление лайка");
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Long filmId, @PathVariable Long userId) {
        log.info("Получен DELETE-запрос на удаление лайка");
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(name = "count", defaultValue = "10") int count) {
        log.info("Получен GET-запрос на вывод популярных фильмов");
        return filmService.getPopularFilms(count);
    }

    @DeleteMapping("/{id}")
    public Film delete(@PathVariable Long filmId) {
        log.info("Получен DELETE-запрос на удаление фильма с ID={}", filmId);
        return filmStorage.delete(filmId);
    }
}
