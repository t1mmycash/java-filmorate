package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;

public interface UserStorage {
    public User addUser(User user);

    public User updateUser(User user);

    public ArrayList<User> getAllUsers();

    public User getUserById(Long id);

    public HashMap<Long, User> getUsers();

    public void userExistenceCheck(long id);
}
