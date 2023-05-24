package ru.practicum.shareit.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {
    private long id;
    @NotBlank(message = "Поле Имя не должно быть пустым")
    private String name;
    @Email
    @NotBlank(message = "Поле email не должно быть пустым")
    private String email;
}