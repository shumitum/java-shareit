package ru.practicum.shareit.user;

import java.util.List;

public interface UserRepository {
    User createUser(User user);

    User getUserById(long id);

    List<User> getAllUsers();

    User updateUser(long userId, User user);

    void deleteUserById(long id);
}