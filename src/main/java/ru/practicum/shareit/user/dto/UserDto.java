package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validationgroup.Create;
import ru.practicum.shareit.validationgroup.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UserDto {
    private long id;
    @NotBlank(groups = Create.class, message = "Поле Имя не должно быть пустым")
    private String name;
    @NotBlank(groups = Create.class, message = "Поле email не должно быть пустым")
    @Email(groups = {Create.class, Update.class})
    private String email;
}