package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserValidateService userValidateService;
    private final UserService userService;

    @PostMapping
    public User createuser(@RequestBody @Valid User user) {
        userValidateService.validateEmail(user.getEmail());
        User newUser = userService.createUser(user);
        log.info("Создан пользователь: {}", newUser);
        return newUser;
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable long userId) {
        User user = userService.getUserById(userId);
        log.info("Запрошен пользователь с ID={} {}", userId, user);
        return user;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Запрошен список всех пользователей");
        return userService.getAllUsers();
    }

    @PatchMapping("/{userId}")
    public User updateUser(@PathVariable long userId,
                           @RequestBody User user) {
        User updatedUser = userService.updateUser(userId, user);
        log.info("Данные пользователя c ID={} обновлены: {}", userId, updatedUser);
        return updatedUser;
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable long userId) {
        userService.deleteUserById(userId);
        log.info("Пользователь с ID={} удалён", userId);
    }
}