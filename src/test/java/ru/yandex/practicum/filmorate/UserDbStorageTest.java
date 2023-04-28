package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureCache
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    private final UserDbStorage userStorage;
    private User firstUser;
    private User secondUser;

    @BeforeEach
    public void beforeEach() {
        firstUser = User.builder()
                .name("Alex White")
                .login("FirstUser")
                .email("firstuser@yandex.ru")
                .birthday(LocalDate.of(1984, 7, 8))
                .build();

        secondUser = User.builder()
                .name("Alex Black")
                .login("SecondUser")
                .email("seconduser@yandex.ru")
                .birthday(LocalDate.of(1984, 8, 9))
                .build();
    }

    @Test
    public void testCreateUserAndGetUserById() {
        firstUser = userStorage.create(firstUser);
        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(firstUser.getId()));
        assertThat(userOptional)
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", firstUser.getId())
                                .hasFieldOrPropertyWithValue("name", "Alex White"));
    }

    @Test
    public void testGetUsers() {
        firstUser = userStorage.create(firstUser);
        secondUser = userStorage.create(secondUser);
        List<User> listUsers = userStorage.getUsers();
        assertThat(listUsers).contains(firstUser);
        assertThat(listUsers).contains(secondUser);
    }

    @Test
    public void testUpdateUser() {
        firstUser = userStorage.create(firstUser);
        User updateUser = User.builder()
                .id(firstUser.getId())
                .name("Alex R. White")
                .login("FirstUser")
                .email("firstuser@yandex.ru")
                .birthday(LocalDate.of(1984, 7, 8))
                .build();
        Optional<User> testUpdateUser = Optional.ofNullable(userStorage.update(updateUser));
        assertThat(testUpdateUser)
                .hasValueSatisfying(user -> assertThat(user)
                        .hasFieldOrPropertyWithValue("name", "Alex R. White")
                );
    }

    @Test
    public void deleteUser() {
        firstUser = userStorage.create(firstUser);
        userStorage.delete(firstUser.getId());
        List<User> listUsers = userStorage.getUsers();
        assertThat(listUsers).hasSize(0);
    }
}
