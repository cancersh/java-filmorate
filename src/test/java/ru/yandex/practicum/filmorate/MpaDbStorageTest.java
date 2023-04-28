package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureCache
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDbStorageTest {
    private final MpaStorage mpaStorage;

    @Test
    public void testGetMpaById() {
        Optional<Mpa> mpaOptional = Optional.ofNullable(mpaStorage.getMpaById(1));
        assertThat(mpaOptional).isPresent()
                .hasValueSatisfying(mpa -> assertThat(mpa)
                        .hasFieldOrPropertyWithValue("name", "G"));
    }

    @Test
    public void testGetAllMpa() {
        assertEquals(5, mpaStorage.getAllMpa().size());
    }
}
