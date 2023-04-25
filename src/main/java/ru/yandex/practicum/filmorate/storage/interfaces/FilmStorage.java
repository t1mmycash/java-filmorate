package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    public Film addFilm(Film film);

    public Film updateFilm(Film film);

    public Film getFilmById(int id);

    public List<Film> getAllFilms();

    public void filmExistenceCheck(int id);

    public List<Film> getPopularFilms(int count);
}
