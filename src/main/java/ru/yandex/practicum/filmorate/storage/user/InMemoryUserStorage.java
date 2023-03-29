package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private static long id;
    public final Map<Long, User> users;

    public InMemoryUserStorage() {
        id = 0L;
        users = new HashMap<>();
    }

    private long nextId() {
        return ++id;
    }

    @Override
    public Collection<User> getUsers() {
        log.info("Количество пользователей: {}", users.size());
        return users.values();
    }

    @Override
    public User create(User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        } else {
            user.setId(nextId());
            users.put(user.getId(), user);
            log.info("Пользователь с адресом электронной почты {} создан", user.getEmail());
        }
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь с ID=" + user.getId() + " не найден!");
        }
        users.put(user.getId(), user);
        log.info("Пользователь с адресом электронной почты {} обновлен", user.getEmail());
        return user;
    }

    @Override
    public User getUserById(long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователь с ID=" + userId + " не найден!");
        }
        return users.get(userId);
    }

    @Override
    public User delete(long userId) {
        if (!users.containsKey(userId)) {
            throw new ValidationException("Пользователь с ID=" + userId + " не найден!");
        }
        // удаляем юзера из списка друзей других юзеров
        for (User user : users.values()) {
            user.getFriends().remove(userId);
        }
        return users.remove(userId);
    }
}