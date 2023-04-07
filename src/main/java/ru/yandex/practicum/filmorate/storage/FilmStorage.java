package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;

public interface FilmStorage {
    public Film addFilm(Film film);

    public Film updateFilm(Film film);

    public Film getFilmById(long id);

    public ArrayList<Film> getAllFilms();

    public void filmExistenceCheck(long id);

    public HashMap<Long, Film> getFilms();
}
