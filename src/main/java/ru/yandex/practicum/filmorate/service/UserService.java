package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public String addFriend(long id, long friendId) {
        userStorage.userExistenceCheck(id);
        userStorage.userExistenceCheck(friendId);
        userStorage.getUserById(id).getFriends().add(friendId);
        userStorage.getUserById(friendId).getFriends().add(id);
        return "Друг был успешно добавлен";
    }

    public String deleteFriend(long id, long friendId) {
        userStorage.userExistenceCheck(id);
        userStorage.userExistenceCheck(friendId);
        userStorage.getUserById(id).getFriends().remove(friendId);
        userStorage.getUserById(friendId).getFriends().remove(id);
        return "Друг был успешно удалён";
    }

    public Collection<User> getFriendsById(Long id) {
        userStorage.userExistenceCheck(id);
        ArrayList<User> result = new ArrayList<>();
        for (Long friendId : userStorage.getUserById(id).getFriends()) {
            result.add(userStorage.getUserById(friendId));
        }
        return result;
    }

    public Collection<User> getCommonFriends(long id, long otherId) {
        userStorage.userExistenceCheck(id);
        userStorage.userExistenceCheck(otherId);
        ArrayList<User> result = new ArrayList<>();
        for (Long firstUserFriend : userStorage.getUserById(id).getFriends()) {
            if (userStorage.getUserById(otherId).getFriends().contains(firstUserFriend) && firstUserFriend != otherId) {
                result.add(userStorage.getUserById(firstUserFriend));
            }
        }
        return result;
    }

}
