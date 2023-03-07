package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final HashMap<Integer, User> users = new HashMap<>();
    private int id = 0;

    @PostMapping
    public User addUser(@RequestBody @Valid User user) {
        if (user.getLogin().contains(" ")) {
            String message = "Логин не может содержать пробелы.";
            log.error(message);
            throw new ValidationException(message);
        }
        user.setId(newId());
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.debug("Создан новый пользователь:\n" + user.toString());
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        if (user.getLogin().contains(" ")) {
            String message = "Логин не может содержать пробелы.";
            log.error(message);
            throw new ValidationException(message);
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.debug("Пользователь:\n{}\nБыл изменён на:\n{}", users.get(user.getId()).toString(), user.toString());
        users.remove(user.getId());
        users.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public HashMap<Integer, User> getUsers() {
        return users;
    }

    private int newId() {
        id += 1;
        return id;
    }
}
