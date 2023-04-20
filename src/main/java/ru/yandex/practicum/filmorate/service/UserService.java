package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    @Autowired
    public UserService(@Qualifier("UserDBStorage") UserStorage userStorage,
                       FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        userStorage.userExistenceCheck(user.getId());
        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(int id) {
        userStorage.userExistenceCheck(id);
        return userStorage.getUserById(id);
    }

    public String addFriend(int id, int friendId) {
        userStorage.userExistenceCheck(id);
        userStorage.userExistenceCheck(friendId);
        friendStorage.addFriend(id, friendId);
        return "Друг был успешно добавлен";
    }

    public String deleteFriend(int id, int friendId) {
        userStorage.userExistenceCheck(id);
        userStorage.userExistenceCheck(friendId);
        friendStorage.deleteFriend(id, friendId);
        return "Друг был успешно удалён";
    }

    public List<User> getFriendsById(int id) {
        userStorage.userExistenceCheck(id);
        return friendStorage.getFriendsById(id);
    }

    public List<User> getCommonFriends(int id, int otherId) {
        userStorage.userExistenceCheck(id);
        userStorage.userExistenceCheck(otherId);
        return friendStorage.getCommonFriends(id, otherId);
    }

}
