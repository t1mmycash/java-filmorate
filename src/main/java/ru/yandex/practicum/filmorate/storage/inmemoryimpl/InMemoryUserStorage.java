package ru.yandex.practicum.filmorate.storage.inmemoryimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component("InMemoryUserStorage")
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Integer, User> users = new HashMap<>();
    private int id = 0;

    @Override
    public User addUser(User user) {
        user.setId(newId());
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.debug("Создан новый пользователь:\n" + user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        userExistenceCheck(user.getId());
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.debug("Пользователь:\n{}\nБыл изменён на:\n{}", users.get(user.getId()), user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(int id) {
        userExistenceCheck(id);
        return users.get(id);
    }

    public HashMap<Integer, User> getUsers() {
        return users;
    }

    @Override
    public void userExistenceCheck(int id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(String.format("Пользователь с id=%d не найден", id));
        }
    }

    private int newId() {
        id += 1;
        return id;
    }
}
