package ru.yandex.practicum.filmorate.storage.inmemoryimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component("InMemoryFilmStorage")
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final InMemoryLikeStorage likeStorage;
    final HashMap<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @Autowired
    public InMemoryFilmStorage(InMemoryLikeStorage likeStorage) {
        this.likeStorage = likeStorage;
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
        return likeStorage.getMostLikedFilms().stream()
                .sorted(new Comparator<List<Integer>>() {
                    @Override
                    public int compare(List<Integer> list1, List<Integer> list2) {
                        return Integer.compare(list1.size(), list2.size());
                    }
                })
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

    private int newId() {
        id += 1;
        return id;
    }
}