package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    public Genre getGenreById(int id);

    public List<Genre> getAllGenres();
}
