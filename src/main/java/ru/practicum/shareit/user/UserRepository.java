package ru.practicum.shareit.user;

import java.util.Collection;

public interface UserRepository {
    User createUser(User user);

    User getUserById(long id);

    Collection<User> getAllUsers();

    User updateUser(long userId, User user);

    void deleteUserById(long id);
}