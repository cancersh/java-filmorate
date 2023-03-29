package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    private long id;
    @Email
    private String email;
    @NotBlank
    @Pattern(regexp = "\\S*$") // логин не содержит пробелов
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();

}
