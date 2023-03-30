package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    // Добавление в друзья
    public void addFriend(long userId, long friendId) {
        if (userId == friendId) {
            throw new ValidationException("Нельзя добавить самого себя в друзья!");
        }

        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    // Удаление из друзей
    public void deleteFriend(long userId, long friendId) {
        if (userId == friendId) {
            throw new ValidationException("Нельзя удалить самого себя из друзей!");
        }

        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    // Получить список друзей
    public List<User> getFriends(long userId) {
        User user = userStorage.getUserById(userId);

        return user.getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());    }

    // Получить список общих друзей
    public List<User> getCommonFriends(long firstUserId, long secondUserId) {
        User firstUser = userStorage.getUserById(firstUserId);
        User secondUser = userStorage.getUserById(secondUserId);

        return firstUser.getFriends().stream()
                .filter(secondUser.getFriends()::contains)
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }
}