package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.IllegalEmailException;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserValidateService {
    private final UserRepository userRepository;

    public void validateEmail(String email) {
        boolean isEmailUnique = userRepository.getAllUsers().stream()
                .noneMatch(user -> (email.equals(user.getEmail())));
        if (!isEmailUnique) {
            throw new IllegalEmailException("Пользователь с данным Email уже существует");
        }
    }
}