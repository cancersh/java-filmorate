package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    // Поставить фильму лайк
    public void addLike(long filmId, long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        if (film != null) {
            if (user != null) {
                film.getLikes().add(userId);
            } else {
                throw new NotFoundException("Пользователь c ID=" + userId + " не найден!");
            }
        } else {
            throw new NotFoundException("Фильм c ID=" + filmId + " не найден!");
        }
    }

    // Убрать у фильма лайк
    public void deleteLike(long filmId, long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        if (film != null) {
            if (film.getLikes().contains(userId)) {
                film.getLikes().remove(userId);
            } else {
                throw new NotFoundException("Лайк от пользователя c ID=" + userId + " не найден!");
            }
        } else {
            throw new NotFoundException("Фильм c ID=" + filmId + " не найден!");
        }
    }

    // Получить список популярных фильмов
    public List<Film> getPopularFilms(long count) {
        return filmStorage.getFilms().stream()
                .sorted(Comparator.comparing(film -> film.getLikes().size(), Comparator.reverseOrder()))
                .limit(count)
                .collect(Collectors.toList());
    }
}