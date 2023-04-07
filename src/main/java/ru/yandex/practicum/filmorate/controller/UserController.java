package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @PostMapping
    public User addUser(@RequestBody @Valid User user) {
        if (user.getLogin().contains(" ")) {
            String message = "Логин не может содержать пробелы.";
            log.error(message);
            throw new ValidationException(message);
        }
        return userStorage.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        if (user.getLogin().contains(" ")) {
            String message = "Логин не может содержать пробелы.";
            log.error(message);
            throw new ValidationException(message);
        }
        return userStorage.updateUser(user);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public String addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        checkId(id);
        checkId(friendId);
        return userService.addFriend(id, friendId);
    }

    @GetMapping
    public ArrayList<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        if (id == null) {
            throw new ValidationException("id не может быть null");
        }
        return userStorage.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriendsById(@PathVariable Long id) {
        checkId(id);
        return userService.getFriendsById(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        checkId(id);
        checkId(otherId);
        return userService.getCommonFriends(id, otherId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public String deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        checkId(id);
        checkId(friendId);
        return userService.deleteFriend(id, friendId);
    }

    private void checkId(Long id) {
        if (id == null) {
            throw new ValidationException("id не может быть null");
        }
    }
}
