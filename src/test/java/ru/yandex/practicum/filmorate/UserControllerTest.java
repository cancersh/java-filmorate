package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private User user;
    private UserController userController;
    private UserStorage userStorage;


    @BeforeEach
    protected void beforeEach() {
        userController = new UserController(userStorage,  new UserService(userStorage));
        user = new User();
        user.setId(1);
        user.setLogin("Shurochka");
        user.setName("Alex Petrov");
        user.setBirthday(LocalDate.of(1989, 7, 13));
        user.setEmail("shurochka@yandex.ru");
    }

    @Test
    protected void validateEmailNullTest() {
        user.setEmail(null);
        Exception exception = assertThrows(ValidationException.class, () -> userController.update(user));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", exception.getMessage());
    }

    @Test
    protected void validateBlankEmailTest() {
        user.setEmail("");
        Exception exception = assertThrows(ValidationException.class, () -> userController.update(user));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", exception.getMessage());
    }

    @Test
    protected void validateEmailTest() {
        user.setEmail("shurochka.yandex.ru");
        Exception exception = assertThrows(ValidationException.class, () -> userController.update(user));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", exception.getMessage());
    }

    @Test
    protected void validateLoginNullTest() {
        user.setLogin(null);
        Exception exception = assertThrows(ValidationException.class, () -> userController.update(user));
        assertEquals("Логин не может быть пустым и содержать пробелы.", exception.getMessage());
    }

    @Test
    protected void validateLoginTest() {
        user.setLogin("shuro chka");
        Exception exception = assertThrows(ValidationException.class, () -> userController.update(user));
        assertEquals("Логин не может быть пустым и содержать пробелы.", exception.getMessage());
    }

    @Test
    protected void validateBlankNameTest() throws ValidationException {
        userController.create(user);
        user.setName("");
        userController.update(user);
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    protected void validateNameNullTest() throws ValidationException {
        userController.create(user);
        user.setName(null);
        userController.update(user);
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    protected void validateBirthdayTest() {
        user.setBirthday(LocalDate.of(2089, 7, 13));
        Exception exception = assertThrows(ValidationException.class, () -> userController.update(user));
        assertEquals("Дата рождения не может быть в будущем.", exception.getMessage());
    }

    @Test
    protected void validateIdTest() {
        user.setId(-1);
        Exception exception = assertThrows(ValidationException.class, () -> userController.update(user));
        assertEquals("Id не может быть отрицательным.", exception.getMessage());
    }
}
