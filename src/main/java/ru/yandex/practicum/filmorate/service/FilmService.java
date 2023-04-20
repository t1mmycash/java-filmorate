package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmGenresStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.LikeStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;
    private final FilmGenresStorage filmGenresStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDBStorage") FilmStorage filmStorage,
                       @Qualifier("UserDBStorage") UserStorage userStorage,
                       LikeStorage likeStorage,
                       FilmGenresStorage filmGenresStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
        this.filmGenresStorage = filmGenresStorage;
    }

    public Film addFilm(Film film) {
        Film result = filmStorage.addFilm(film);
        filmGenresStorage.addAllFilmGenres(film);
        return result;
    }

    public Film updateFilm(Film film) {
        filmStorage.filmExistenceCheck(film.getId());
        ArrayList<Genre> distinctList = new ArrayList<>();
        for (Genre genre : film.getGenres()) {
            if (!distinctList.contains(genre)) {
                distinctList.add(genre);
            }
        }
        film.setGenres(distinctList);
        filmStorage.updateFilm(film);
        filmGenresStorage.updateFilmGenres(film);
        return film;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(int id) {
        filmStorage.filmExistenceCheck(id);
        return filmStorage.getFilmById(id);
    }


    public String like(int filmId, int userId) {
        filmStorage.filmExistenceCheck(filmId);
        userStorage.userExistenceCheck(userId);
        return likeStorage.like(filmId, userId);
    }

    public String removeLike(int filmId, int userId) {
        filmStorage.filmExistenceCheck(filmId);
        userStorage.userExistenceCheck(userId);
        return likeStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

}
