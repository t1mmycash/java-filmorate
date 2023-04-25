package ru.yandex.practicum.filmorate.storage.inmemoryimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component("InMemoryFriendStorage")
public class InMemoryFriendStorage implements FriendStorage {

    private final UserStorage userStorage;
    private final HashMap<Integer, ArrayList<Integer>> friends = new HashMap<>();

    @Autowired
    public InMemoryFriendStorage(@Qualifier("InMemoryUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public String addFriend(int id, int friendId) {
        userStorage.userExistenceCheck(id);
        userStorage.userExistenceCheck(friendId);
        if (!friends.containsKey(id)) {
            friends.put(id, new ArrayList<>());
        }
        friends.get(id).add(friendId);
        return "Друг был успешно добавлен";
    }

    @Override
    public String deleteFriend(int id, int friendId) {
        userStorage.userExistenceCheck(id);
        userStorage.userExistenceCheck(friendId);
        friends.get(id).remove((Integer) friendId);
        return "Друг был успешно удалён";
    }

    @Override
    public List<User> getFriendsById(int id) {
        userStorage.userExistenceCheck(id);
        ArrayList<User> result = new ArrayList<>();
        for (Integer friendId : friends.get(id)) {
            result.add(userStorage.getUserById(friendId));
        }
        return result;
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        userStorage.userExistenceCheck(id);
        userStorage.userExistenceCheck(otherId);
        ArrayList<Integer> helpList = new ArrayList<>();
        helpList.addAll(friends.get(id));
        helpList.addAll(friends.get(otherId));
        ArrayList<Integer> commonFriendsId = new ArrayList<>();
        for (int userId : helpList) {
            if (!commonFriendsId.contains(userId) && userId != id && userId != otherId) {
                commonFriendsId.add(userId);
            }
        }
        return commonFriendsId.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }
}
