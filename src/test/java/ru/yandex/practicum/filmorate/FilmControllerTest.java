package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    private Film film;
    private FilmController filmController;
    private static final String WRONG_DESCRIPTION = "Ложное описание ложное описание ложное описание " +
            "ложное описание ложное описание ложное описание ложное описание ложное описание ложное описание " +
            "ложное описание ложное описание ложное описание ложное описание";


    @BeforeEach
    protected void beforeEach() {
        filmController = new FilmController();
        film = new Film();
        film.setName("Зеленая миля");
        film.setDescription("В тюрьме для смертников появляется заключенный с божественным даром. " +
                "Мистическая драма по роману Стивена Кинга");
        film.setReleaseDate(LocalDate.of(1999, 12, 6));
        film.setDuration(189);
    }

    @Test
    protected void validateNameNullTest() {
        film.setName(null);
        Exception exception = assertThrows(ValidationException.class, () -> filmController.update(film));
        assertEquals("Название фильма не может быть пустым.", exception.getMessage());
    }

    @Test
    protected void validateBlankNameTest() {
        film.setName("");
        Exception exception = assertThrows(ValidationException.class, () -> filmController.update(film));
        assertEquals("Название фильма не может быть пустым.", exception.getMessage());
    }

    @Test
    protected void validateDescriptionMore200Test() {
        film.setDescription(WRONG_DESCRIPTION);
        Exception exception = assertThrows(ValidationException.class, () -> filmController.update(film));
        assertEquals("Максимальная длина описания — 200 символов.", exception.getMessage());
    }

    @Test
    protected void validateIdTest() {
        film.setId(-1);
        Exception exception = assertThrows(ValidationException.class, () -> filmController.update(film));
        assertEquals("Id не может быть отрицательным.", exception.getMessage());
    }

    @Test
    protected void validateDurationTest() {
        film.setDuration(-189);
        Exception exception = assertThrows(ValidationException.class, () -> filmController.update(film));
        assertEquals("Продолжительность фильма должна быть положительной.", exception.getMessage());
    }

    @Test
    protected void validateReleaseTest() {
        film.setReleaseDate(LocalDate.of(1672, 6,9));
        Exception exception = assertThrows(ValidationException.class, () -> filmController.update(film));
        assertEquals("Дата релиза фильма не может быть раньше 28 декабря 1895 года.", exception.getMessage());
    }
}
