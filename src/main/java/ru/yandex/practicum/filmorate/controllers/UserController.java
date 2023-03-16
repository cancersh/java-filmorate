package ru.yandex.practicum.filmorate.controllers;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @ResponseBody
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Получен POST-запрос на добавление пользователя");
        validateUser(user);
        if (users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь " + user.getEmail() + " уже зарегистрирован.");
        } else {
            user.setId(++id);
            users.put(user.getId(), user);
        }
        return user;
    }

    @ResponseBody
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Получен PUT-запрос на обновление пользователя с ID={}", user.getId());
        validateUser(user);
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с id=" + user.getId() + " не найден.");
        } else {
            users.put(user.getId(), user);
        }
        return user;
    }

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
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
