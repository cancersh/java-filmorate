package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotations.MinimumDate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Builder
public class Film {

    private Long id;
    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;
    @Size(max = 200)
    private String description;
    @NotNull
    @MinimumDate
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private Set<Long> likes;
    @NotNull
    private Mpa mpa;
    private Set<Genre> genres;

    public Film(Long id, String name, String description, LocalDate releaseDate, Integer duration,
                Set<Long> likes, Mpa mpa, Set<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = likes;
        this.mpa = mpa;
        this.genres = genres;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("release_Date", releaseDate);
        values.put("duration", duration);
        values.put("rating_id", mpa.getId());
        return values;
    }
}
