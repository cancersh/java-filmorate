package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

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
        List<User> friends = new ArrayList<>();

        if (user != null) {
            for (Long friend : user.getFriends()) {
                friends.add(userStorage.getUserById(friend));
            }
        }
        return friends;
    }

    // Получить список общих друзей
    public List<User> getCommonFriends(long firstUserId, long secondUserId) {
        User firstUser = userStorage.getUserById(firstUserId);
        User secondUser = userStorage.getUserById(secondUserId);
        List<User> commonFriends = null;

        if ((firstUser != null) && (secondUser != null)) {
            commonFriends = new ArrayList<>(getFriends(firstUserId));
            commonFriends.retainAll(getFriends(secondUserId));
        }
        return commonFriends;
    }
}