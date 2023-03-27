package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage{

    private final HashMap<Long, User> users = new HashMap<>();
    private long id = 0;

    @Override
    public User addUser(User user) {
        user.setId(newId());
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if(user.getFriends() == null) {
            user.setFriends(new HashSet<>());
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
        if(user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        log.debug("Пользователь:\n{}\nБыл изменён на:\n{}", users.get(user.getId()), user);
        users.remove(user.getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long id) {
        userExistenceCheck(id);
        return users.get(id);
    }

    @Override
    public HashMap<Long, User> getUsers() {
        return users;
    }

    @Override
    public void userExistenceCheck(long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(String.format("Пользователь с id=%d не найден", id));
        }
    }

    private Long newId() {
        id += 1;
        return id;
    }
}
