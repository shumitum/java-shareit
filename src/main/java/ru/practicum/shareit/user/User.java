package ru.practicum.shareit.user;

import lombok.Data;
import ru.practicum.shareit.validationGroup.Create;
import ru.practicum.shareit.validationGroup.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class User {
    private long id;
    @NotBlank(groups = Create.class, message = "Поле Имя не должно быть пустым")
    private String name;
    @NotBlank(groups = Create.class, message = "Поле email не должно быть пустым")
    @Email(groups = {Create.class, Update.class})
    private String email;
}