package ru.yandex.practicum.filmorate.controllers;

import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final List<User> users = new ArrayList<>();

    @ResponseBody
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Получен POST-запрос на добавление пользователя");
        validateUser(user);
        users.add(user);
        return user;
    }

    @ResponseBody
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Получен PUT-запрос на обновление пользователя с ID={}", user.getId());
        validateUser(user);
        users.set(user.getId(), user);
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        return users;
    }

    public void validateUser(User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().isBlank() || !(user.getEmail().contains("@"))) {
            log.info("Получено исключение: Электронная почта не может быть пустой и должна содержать символ @.");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @.");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.info("Получено исключение: Логин не может быть пустым и содержать пробелы.");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Получено исключение: Дата рождения не может быть в будущем.");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        if (user.getId() < 0) {
            log.info("Получено исключение: id не может быть отрицательным.");
            throw new ValidationException("Id не может быть отрицательным.");
        }
    }
}
