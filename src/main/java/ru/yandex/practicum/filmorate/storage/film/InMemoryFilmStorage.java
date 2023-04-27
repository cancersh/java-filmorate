package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private static Long filmId;
    public final Map<Long, Film> films;

    public InMemoryFilmStorage() {
        filmId = 0L;
        films = new HashMap<>();
    }

    private Long nextId() {
        return ++filmId;
    }

    @Override
    public Collection<Film> getFilms() {
        log.info("Количество фильмов: {}", films.size());
        return films.values();
    }

    @Override
    public Film create(Film film) {
        if (films.containsKey(film.getId())) {
            throw new ValidationException("Фильм \"" +
                    film.getName() + "\" уже есть в списке.");
        }
        if (isValidateFilm(film)) {
            film.setId(nextId());
            films.put(film.getId(), film);
            log.info("Фильм {} создан.", film.getName());
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        if (film.getId() == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм с ID=" + film.getId() + " не найден!");
        }
        if (isValidateFilm(film)) {
            films.put(film.getId(), film);
            log.info("Фильм {} обновлен.", film.getName());
        }
        return film;
    }

    @Override
    public Film getFilmById(Long filmId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException("Фильм с ID=" + filmId + " не найден!");
        }

        return films.get(filmId);
    }

    @Override
    public Film delete(Long filmId) {
        if (filmId == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        if (!films.containsKey(filmId)) {
            throw new ValidationException("Фильм с ID=" + filmId + " не найден!");
        }

        log.info("Фильм {} удален.", films.get(filmId).getName());
        return films.remove(filmId);
    }

    private boolean isValidateFilm(Film film) {
        if (film.getName().isEmpty()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if ((film.getDescription().length()) > 200 || (film.getDescription().isEmpty())) {
            throw new ValidationException("Описание фильма больше 200 символов или пустое: " + film.getDescription().length());
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза фильма не может быть раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
        return true;
    }
}