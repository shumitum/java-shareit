package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validationgroup.Create;
import ru.practicum.shareit.validationgroup.Update;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserClient userClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createUser(@RequestBody @Validated(Create.class) UserDto userDto) {
        ResponseEntity<Object> newUserDto = userClient.createUser(userDto);
        log.info("Создан пользователь: {}", newUserDto);
        return newUserDto;
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getUserById(@PathVariable long userId) {
        ResponseEntity<Object> userDto = userClient.getUserById(userId);
        log.info("Запрошен пользователь с ID={} {}", userId, userDto);
        return userDto;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllUsers() {
        log.info("Запрошен список всех пользователей");
        return userClient.getAllUsers();
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateUser(@PathVariable long userId,
                                             @RequestBody @Validated(Update.class) UserDto userDto) {
        ResponseEntity<Object> updatedUserDto = userClient.updateUser(userId, userDto);
        log.info("Данные пользователя c ID={} обновлены: {}", userId, updatedUserDto);
        return updatedUserDto;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserById(@PathVariable long userId) {
        userClient.deleteUserById(userId);
        log.info("Пользователь с ID={} удалён", userId);
    }
}