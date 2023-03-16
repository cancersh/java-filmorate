package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {

    private int id;
    @Email
    private String email;
    @NotBlank
    @Pattern(regexp = "\\S*$") // логин не содержит пробелов
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;

}
