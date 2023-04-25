package ru.yandex.practicum.filmorate.storage.inmemoryimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.LikeStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("InMemoryFilmStorage")
@Slf4j
public class InMemoryFilmStorage implements FilmStorage, LikeStorage {

    final HashMap<Integer, Film> films = new HashMap<>();
    private int id = 0;
    private final HashMap<Integer, ArrayList<Integer>> filmLikes = new HashMap<>();
    private final UserStorage userStorage;

    @Autowired
    public InMemoryFilmStorage(@Qualifier("InMemoryUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(newId());
        films.put(film.getId(), film);
        log.debug("Новый фильм:\n" + film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        filmExistenceCheck(film.getId());
        log.debug("Фильм:\n{}\nЗаменен на:{}", films.get(film.getId()), film);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(int id) {
        filmExistenceCheck(id);
        return films.get(id);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        List<ArrayList<Integer>> result = new ArrayList<>();
        for (Map.Entry<Integer, ArrayList<Integer>> film : filmLikes.entrySet()) {
            ArrayList<Integer> likes = new ArrayList<>();
            likes.add(film.getKey());
            result.add(film.getValue());
        }
        return result.stream()
                .limit(count)
                .map(e -> e.get(e.size() - 1))
                .map(films::get)
                .collect(Collectors.toList());
    }

    @Override
    public void filmExistenceCheck(int id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException(String.format("Фильм с id=%d не найден", id));
        }
    }

    @Override
    public String like(int filmId, int userId) {
        filmExistenceCheck(filmId);
        userStorage.userExistenceCheck(userId);
        if (!filmLikes.containsKey(filmId)) {
            filmLikes.put(filmId, new ArrayList<>());
        }
        filmLikes.get(filmId).add(userId);
        return "Лайк добавлен";
    }

    @Override
    public String removeLike(int filmId, int userId) {
        filmExistenceCheck(filmId);
        userStorage.userExistenceCheck(userId);
        filmLikes.get(filmId).remove((Integer) userId);
        return "Лайк успешно удален";
    }

    private int newId() {
        id += 1;
        return id;
    }
}