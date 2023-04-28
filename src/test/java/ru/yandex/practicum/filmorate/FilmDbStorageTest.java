package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureCache
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final FilmService filmService;
    private User firstUser;
    private User secondUser;
    private User thirdUser;
    private Film firstFilm;
    private Film secondFilm;
    private Film thirdFilm;

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

        secondFilm = Film.builder()
                .name("Список Шиндлера")
                .description("История немецкого промышленника, спасшего тысячи жизней во время Холокоста. " +
                        "Драма Стивена Спилберга")
                .releaseDate(LocalDate.of(1993, 11, 30))
                .duration(195)
                .build();
        secondFilm.setMpa(new Mpa(4, "R"));
        secondFilm.setLikes(new HashSet<>());
        secondFilm.setGenres(new HashSet<>(Arrays.asList(new Genre(2, "Драма"))));

        thirdFilm = Film.builder()
                .name("Форрест Гамп")
                .description("Полувековая история США глазами чудака из Алабамы. " +
                        "Абсолютная классика Роберта Земекиса с Томом Хэнксом")
                .releaseDate(LocalDate.of(1994, 6, 23))
                .duration(142)
                .build();
        thirdFilm.setMpa(new Mpa(3, "PG-13"));
        thirdFilm.setLikes(new HashSet<>());
        thirdFilm.setGenres(new HashSet<>(Arrays.asList(new Genre(2, "Драма"),
                new Genre(1, "Комедия"))));
    }

    @Test
    public void testCreateFilmAndGetFilmById() {
        firstFilm = filmStorage.create(firstFilm);
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilmById(firstFilm.getId()));
        assertThat(filmOptional)
                .hasValueSatisfying(film -> assertThat(film)
                        .hasFieldOrPropertyWithValue("id", firstFilm.getId())
                        .hasFieldOrPropertyWithValue("name", "Зеленая миля")
                );
    }

    @Test
    public void testGetFilms() {
        firstFilm = filmStorage.create(firstFilm);
        secondFilm = filmStorage.create(secondFilm);
        thirdFilm = filmStorage.create(thirdFilm);
        List<Film> listFilms = filmStorage.getFilms();
        assertThat(listFilms).contains(firstFilm);
        assertThat(listFilms).contains(secondFilm);
        assertThat(listFilms).contains(thirdFilm);
    }

    @Test
    public void testUpdateFilm() {
        firstFilm = filmStorage.create(firstFilm);
        Film updateFilm = Film.builder()
                .id(firstFilm.getId())
                .name("Update Name")
                .description("Update Description")
                .releaseDate(LocalDate.of(1999, 12, 6))
                .duration(189)
                .build();
        updateFilm.setMpa(new Mpa(5, "NC-17"));
        Optional<Film> testUpdateFilm = Optional.ofNullable(filmStorage.update(updateFilm));
        assertThat(testUpdateFilm)
                .hasValueSatisfying(film ->
                        assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Update Name")
                                .hasFieldOrPropertyWithValue("description", "Update Description")
                );
    }

    @Test
    public void deleteFilm() {
        firstFilm = filmStorage.create(firstFilm);
        secondFilm = filmStorage.create(secondFilm);
        filmStorage.delete(firstFilm.getId());
        List<Film> listFilms = filmStorage.getFilms();
        assertThat(listFilms).hasSize(1);
        assertThat(Optional.of(listFilms.get(0)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Список Шиндлера"));
    }

    @Test
    public void testGetPopularFilms() {
        firstUser = userStorage.create(firstUser);
        secondUser = userStorage.create(secondUser);
        thirdUser = userStorage.create(thirdUser);

        firstFilm = filmStorage.create(firstFilm);
        filmService.addLike(firstFilm.getId(), firstUser.getId());

        secondFilm = filmStorage.create(secondFilm);
        filmService.addLike(secondFilm.getId(), firstUser.getId());
        filmService.addLike(secondFilm.getId(), secondUser.getId());
        filmService.addLike(secondFilm.getId(), thirdUser.getId());

        thirdFilm = filmStorage.create(thirdFilm);
        filmService.addLike(thirdFilm.getId(), firstUser.getId());
        filmService.addLike(thirdFilm.getId(), secondUser.getId());

        List<Film> listFilms = filmService.getPopularFilms(5);

        assertThat(listFilms).hasSize(3);

        assertThat(Optional.of(listFilms.get(0)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Список Шиндлера"));

        assertThat(Optional.of(listFilms.get(1)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Форрест Гамп"));

        assertThat(Optional.of(listFilms.get(2)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Зеленая миля"));
    }
}
