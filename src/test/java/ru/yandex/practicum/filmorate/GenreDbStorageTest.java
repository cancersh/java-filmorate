package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
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
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureCache
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTest {
    private final GenreStorage genreStorage;
    private final FilmDbStorage filmStorage;
    private Film firstFilm;

    @BeforeEach
    public void beforeEach() {
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
    public void testGetGenreById() {
        Optional<Genre> genreOptional = Optional.ofNullable(genreStorage.getGenreById(1));
        assertThat(genreOptional).isPresent()
                .hasValueSatisfying(genre -> assertThat(genre)
                        .hasFieldOrPropertyWithValue("name", "Комедия"));
    }

    @Test
    public void testGetAllGenres() {
        assertEquals(6, genreStorage.getGenres().size());
    }

    @Test
    public void testSetAndGetGenre() {
        Set<Genre> genres = new HashSet<>();
        genres.add(new Genre(1, "Комедия"));
        firstFilm = filmStorage.create(firstFilm);
        firstFilm.setGenres(genres);
        assertEquals(1, genreStorage.getFilmGenres(firstFilm.getId()).size());
    }
}
