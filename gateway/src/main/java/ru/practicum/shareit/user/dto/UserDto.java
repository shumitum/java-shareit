package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import ru.practicum.shareit.validationgroup.Create;
import ru.practicum.shareit.validationgroup.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
public class UserDto {
    @NotBlank(groups = Create.class, message = "Поле Имя не должно быть пустым")
    @Size(max = 255)
    private String name;
    @NotBlank(groups = Create.class, message = "Поле email не должно быть пустым")
    @Email(groups = {Create.class, Update.class})
    @Size(max = 255)
    private String email;
}