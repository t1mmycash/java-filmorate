package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface FilmGenresStorage {
    public void updateFilmGenres(Film film);

    public void addAllFilmGenres(Film film);

    public List<Genre> getFilmGenres(int id);
}
