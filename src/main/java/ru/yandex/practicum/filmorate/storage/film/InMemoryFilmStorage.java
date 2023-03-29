package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    public final Map<Long, Film> films;
    private static long filmId;

    public InMemoryFilmStorage() {
        filmId = 0L;
        films = new HashMap<>();
    }

    private long nextId() {
        return ++filmId;
    }

    @Override
    public Collection<Film> getFilms() {
        log.info("Количество фильмов: {}", films.size());
        return films.values();
    }

    @Override
    public Film create(Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            throw new ValidationException("Фильм \"" +
                    film.getName() + "\" уже есть в списке.");
        } else {
            film.setId(nextId());
            films.put(film.getId(), film);
            log.info("Фильм {} создан.", film.getName());
        }
        return film;
    }

    @Override
    public Film update(Film film) throws ValidationException {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм с ID=" + film.getId() + " не найден!");
        }
        films.put(film.getId(), film);
        log.info("Фильм {} обновлен.", film.getName());
        return film;
    }

    @Override
    public Film getFilmById(long filmId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException("Фильм с ID=" + filmId + " не найден!");
        }
        return films.get(filmId);
    }

    @Override
    public Film delete(long filmId) {
        if (!films.containsKey(filmId)) {
            throw new ValidationException("Фильм с ID=" + filmId + " не найден!");
        }
        log.info("Фильм {} удален.", films.get(filmId).getName());
        return films.remove(filmId);
    }
}