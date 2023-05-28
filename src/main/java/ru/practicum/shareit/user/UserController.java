package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.validationGroup.Create;
import ru.practicum.shareit.validationGroup.Update;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserValidateService userValidateService;
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createuser(@RequestBody @Validated(Create.class) User user) {
        userValidateService.validateEmail(user.getEmail());
        User newUser = userService.createUser(user);
        log.info("Создан пользователь: {}", newUser);
        return newUser;
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable long userId) {
        User user = userService.getUserById(userId);
        log.info("Запрошен пользователь с ID={} {}", userId, user);
        return user;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        log.info("Запрошен список всех пользователей");
        return userService.getAllUsers();
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@PathVariable long userId,
                           @RequestBody @Validated(Update.class) User user) {
        User updatedUser = userService.updateUser(userId, user);
        log.info("Данные пользователя c ID={} обновлены: {}", userId, updatedUser);
        return updatedUser;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserById(@PathVariable long userId) {
        userService.deleteUserById(userId);
        log.info("Пользователь с ID={} удалён", userId);
    }
}