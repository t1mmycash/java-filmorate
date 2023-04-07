package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Long, Film> films = new HashMap<>();
    private long id = 0;

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
    public ArrayList<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(long id) {
        filmExistenceCheck(id);
        return films.get(id);
    }

    @Override
    public HashMap<Long, Film> getFilms() {
        return films;
    }

    @Override
    public void filmExistenceCheck(long id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException(String.format("Фильм с id=%d не найден", id));
        }
    }

    private long newId() {
        id += 1;
        return id;
    }
}
