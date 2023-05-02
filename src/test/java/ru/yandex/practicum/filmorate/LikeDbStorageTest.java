package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikeDbStorageTest {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final FilmService filmService;
    private User firstUser;
    private User secondUser;
    private Film firstFilm;

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

        firstFilm = Film.builder()
                .name("Зеленая миля")
                .description("В тюрьме для смертников появляется заключенный с божественным даром. " +
                        "Мистическая драма по роману Стивена Кинга")
                .releaseDate(LocalDate.of(1999, 12, 6))
                .duration(189)
                .build();
        firstFilm.setMpa(new Mpa(4, "R"));
        firstFilm.setLikes(new HashSet<>());
        firstFilm.setGenres(new HashSet<>(Arrays.asList(new Genre(2, "Драма"))));
    }

    @Test
    public void testAddLike() {
        firstUser = userStorage.create(firstUser);
        firstFilm = filmStorage.create(firstFilm);
        filmService.addLike(firstFilm.getId(), firstUser.getId());
        firstFilm = filmStorage.getFilmById(firstFilm.getId());
        assertThat(firstFilm.getLikes()).hasSize(1);
        assertThat(firstFilm.getLikes()).contains(firstUser.getId());
    }

    @Test
    public void testDeleteLike() {
        firstUser = userStorage.create(firstUser);
        secondUser = userStorage.create(secondUser);
        firstFilm = filmStorage.create(firstFilm);
        filmService.addLike(firstFilm.getId(), firstUser.getId());
        filmService.addLike(firstFilm.getId(), secondUser.getId());
        filmService.deleteLike(firstFilm.getId(), firstUser.getId());
        firstFilm = filmStorage.getFilmById(firstFilm.getId());
        assertThat(firstFilm.getLikes()).hasSize(1);
        assertThat(firstFilm.getLikes()).contains(secondUser.getId());
    }
}
