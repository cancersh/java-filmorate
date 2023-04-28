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
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureCache
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendDbStorageTest {
    private final UserDbStorage userStorage;
    private final UserService userService;
    private User firstUser;
    private User secondUser;
    private User thirdUser;

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

        thirdUser = User.builder()
                .name("Alex Red")
                .login("ThirdUser")
                .email("thirduser@yandex.ru")
                .birthday(LocalDate.of(1984, 9, 10))
                .build();
    }
    @Test
    public void testAddFriend() {
        firstUser = userStorage.create(firstUser);
        secondUser = userStorage.create(secondUser);
        userService.addFriend(firstUser.getId(), secondUser.getId());
        assertThat(userService.getFriends(firstUser.getId())).hasSize(1);
        assertThat(userService.getFriends(firstUser.getId())).contains(secondUser);
    }

    @Test
    public void testDeleteFriend() {
        firstUser = userStorage.create(firstUser);
        secondUser = userStorage.create(secondUser);
        thirdUser = userStorage.create(thirdUser);
        userService.addFriend(firstUser.getId(), secondUser.getId());
        userService.addFriend(firstUser.getId(), thirdUser.getId());
        userService.deleteFriend(firstUser.getId(), secondUser.getId());
        assertThat(userService.getFriends(firstUser.getId())).hasSize(1);
        assertThat(userService.getFriends(firstUser.getId())).contains(thirdUser);
    }

    @Test
    public void testGetFriends() {
        firstUser = userStorage.create(firstUser);
        secondUser = userStorage.create(secondUser);
        thirdUser = userStorage.create(thirdUser);
        userService.addFriend(firstUser.getId(), secondUser.getId());
        userService.addFriend(firstUser.getId(), thirdUser.getId());
        assertThat(userService.getFriends(firstUser.getId())).hasSize(2);
        assertThat(userService.getFriends(firstUser.getId())).contains(secondUser, thirdUser);
    }

    @Test
    public void testGetCommonFriends() {
        firstUser = userStorage.create(firstUser);
        secondUser = userStorage.create(secondUser);
        thirdUser = userStorage.create(thirdUser);
        userService.addFriend(firstUser.getId(), secondUser.getId());
        userService.addFriend(firstUser.getId(), thirdUser.getId());
        userService.addFriend(secondUser.getId(), firstUser.getId());
        userService.addFriend(secondUser.getId(), thirdUser.getId());
        assertThat(userService.getCommonFriends(firstUser.getId(), secondUser.getId())).hasSize(1);
        assertThat(userService.getCommonFriends(firstUser.getId(), secondUser.getId()))
                .contains(thirdUser);
    }
}
