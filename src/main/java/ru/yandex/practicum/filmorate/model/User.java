package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

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
