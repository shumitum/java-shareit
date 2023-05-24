package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.NoSuchElementException;

@Component
public class UserRepositoryImpl implements UserRepository {
    private final HashMap<Long, User> users = new HashMap<>();
    private long userId;

    @Override
    public User createUser(User user) {
        user.setId(++userId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(long userId) {
        if (users.containsKey(userId)) {
            return users.get(userId);
        } else {
            throw new NoSuchElementException("Пользователя с ID=" + userId + " не существует");
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User updateUser(long userId, User user) {
        users.put(userId, user);
        return getUserById(userId);
    }

    @Override
    public void deleteUserById(long userId) {
        users.remove(userId);
    }
}