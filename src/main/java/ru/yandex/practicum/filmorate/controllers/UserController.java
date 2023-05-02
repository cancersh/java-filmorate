package ru.yandex.practicum.filmorate.controllers;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(@Qualifier("userDbStorage") UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @ResponseBody
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Получен POST-запрос на добавление пользователя");
        return userStorage.create(user);
    }

    @ResponseBody
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Получен PUT-запрос на обновление пользователя с ID={}", user.getId());
        return userStorage.update(user);
    }

    @DeleteMapping("/{id}")
    public User delete(@PathVariable Long userId) {
        log.info("Получен DELETE-запрос на удаление пользователя с ID={}", userId);
        return userStorage.delete(userId);
    }

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Получен GET-запрос на вывод списка пользователей");
        return userStorage.getUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long userId) {
        log.info("Получен GET-запрос на вывод пользователя с ID={}", userId);
        return userStorage.getUserById(userId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable("id") Long userId) {
        log.info("Получен GET-запрос на вывод друзей пользователя с ID={}", userId);
        return userService.getFriends(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Long userId, @PathVariable("friendId") Long friendId) {
        log.info("Получен PUT-запрос на добавление пользователя с ID={} " +
                "в друзья к пользователю с ID={}", userId, friendId);
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Long userId, @PathVariable("friendId") Long friendId) {
        log.info("Получен DELETE-запрос на удаление из друзей пользователя с ID={} " +
                "у пользователя с ID={}", friendId, userId);
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable("id") Long userId, @PathVariable("otherId") Long otherId) {
        log.info("Получен GET-запрос на вывод общих друзей у пользователей с ID={} и с ID={}", userId, otherId);
        return userService.getCommonFriends(userId, otherId);
    }
}
