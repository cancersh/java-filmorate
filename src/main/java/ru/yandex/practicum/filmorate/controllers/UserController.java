package ru.yandex.practicum.filmorate.controllers;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @ResponseBody
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Получен POST-запрос на добавление пользователя");
        validateUser(user);
        return userStorage.create(user);
    }

    @ResponseBody
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Получен PUT-запрос на обновление пользователя с ID={}", user.getId());
        validateUser(user);
        return userStorage.update(user);
    }

    @DeleteMapping("/{id}")
    public User delete(@PathVariable long userId) {
        log.info("Получен DELETE-запрос на удаление пользователя с ID={}", userId);
        return userStorage.delete(userId);
    }

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Получен GET-запрос на вывод списка пользователей");
        return userStorage.getUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") long userId) {
        log.info("Получен GET-запрос на вывод пользователя с ID={}", userId);
        return userStorage.getUserById(userId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable("id") long userId) {
        log.info("Получен GET-запрос на вывод друзей пользователя с ID={}", userId);
        return userService.getFriends(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") long userId, @PathVariable("friendId") long friendId) {
        log.info("Получен PUT-запрос на добавление пользователя с ID={} " +
                "в друзья к пользователю с ID={}", userId, friendId);
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") long userId, @PathVariable("friendId") long friendId) {
        log.info("Получен DELETE-запрос на удаление из друзей пользователя с ID={} " +
                "у пользователя с ID={}", friendId, userId);
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable("id") long userId, @PathVariable("otherId") long otherId) {
        log.info("Получен GET-запрос на вывод общих друзей у пользователей с ID={} и с ID={}", userId, otherId);
        return userService.getCommonFriends(userId, otherId);
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !(user.getEmail().contains("@"))) {
            log.warn("Получено исключение: Электронная почта не может быть пустой и должна содержать символ @.");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @.");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Получено исключение: Логин не может быть пустым и содержать пробелы.");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Получено исключение: Дата рождения не может быть в будущем.");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        if (user.getId() < 0) {
            log.warn("Получено исключение: id={} не может быть отрицательным.", user.getId());
            throw new ValidationException("Id не может быть отрицательным.");
        }
    }
}
