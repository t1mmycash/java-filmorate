package ru.yandex.practicum.filmorate.storage.interfaces;

public interface LikeStorage {
    public String like(int filmId, int userId);

    public String removeLike(int filmId, int userId);
}
