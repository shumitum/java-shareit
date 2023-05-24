package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserValidateService userValidateService;

    @Override
    public User createUser(User user) {
        return userRepository.createUser(user);
    }

    @Override
    public User getUserById(long userId) {
        return userRepository.getUserById(userId);
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public User updateUser(long userId, User user) {
        User updatingUser = userRepository.getUserById(userId);
        if (user.getEmail() != null) {
            if (!updatingUser.getEmail().equals(user.getEmail())) {
                userValidateService.validateEmail(user.getEmail());
                updatingUser.setEmail(user.getEmail());
            }
        }
        if (user.getName() != null) {
            updatingUser.setName(user.getName());
        }
        return userRepository.updateUser(userId, updatingUser);
    }

    @Override
    public void deleteUserById(long userId) {
        userRepository.getUserById(userId);
        userRepository.deleteUserById(userId);
    }
}