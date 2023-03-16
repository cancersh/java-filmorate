package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private User user;
    private UserController userController;

    @BeforeEach
    protected void beforeEach() {
        userController = new UserController();
        user = new User();
        user.setLogin("Shurochka");
        user.setName("Alex Petrov");
        user.setBirthday(LocalDate.of(1989, 7, 13));
        user.setEmail("shurochka@yandex.ru");
    }

    @Test
    protected void validateEmailNullTest() {
        user.setEmail(null);
        Exception exception = assertThrows(ValidationException.class, () -> userController.validateUser(user));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", exception.getMessage());
    }

    @Test
    protected void validateBlankEmailTest() {
        user.setEmail("");
        Exception exception = assertThrows(ValidationException.class, () -> userController.validateUser(user));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", exception.getMessage());
    }

    @Test
    protected void validateEmailTest() {
        user.setEmail("shurochka.yandex.ru");
        Exception exception = assertThrows(ValidationException.class, () -> userController.validateUser(user));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", exception.getMessage());
    }

    @Test
    protected void validateLoginNullTest() {
        user.setLogin(null);
        Exception exception = assertThrows(ValidationException.class, () -> userController.validateUser(user));
        assertEquals("Логин не может быть пустым и содержать пробелы.", exception.getMessage());
    }

    @Test
    protected void validateLoginTest() {
        user.setLogin("shuro chka");
        Exception exception = assertThrows(ValidationException.class, () -> userController.validateUser(user));
        assertEquals("Логин не может быть пустым и содержать пробелы.", exception.getMessage());
    }

    @Test
    protected void validateBlankNameTest() throws ValidationException {
        user.setName("");
        userController.validateUser(user);
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    protected void validateNameNullTest() throws ValidationException {
        user.setName(null);
        userController.validateUser(user);
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    protected void validateBirthdayTest() {
        user.setBirthday(LocalDate.of(2089, 7, 13));
        Exception exception = assertThrows(ValidationException.class, () -> userController.validateUser(user));
        assertEquals("Дата рождения не может быть в будущем.", exception.getMessage());
    }

    @Test
    protected void validateIdTest() {
        user.setId(-1);
        Exception exception = assertThrows(ValidationException.class, () -> userController.validateUser(user));
        assertEquals("Id не может быть отрицательным.", exception.getMessage());
    }
}
