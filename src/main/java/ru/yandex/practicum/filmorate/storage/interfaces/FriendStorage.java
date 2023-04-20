package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {
    public String addFriend(int id, int friendId);

    public String deleteFriend(int id, int friendId);

    public List<User> getFriendsById(int id);

    public List<User> getCommonFriends(int id, int otherId);
}
