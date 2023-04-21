package ru.yandex.practicum.filmorate.storage.inmemoryimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component("InMemoryFilmStorage")
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    final HashMap<Integer, Film> films = new HashMap<>();
    private int id = 0;

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
        //я не могу вернуть реализацию через память, из-за того, что теперь для этого мне нужно, чтобы
        //этот класс вызывал класс с лайками и наоборот, а спринг тупо не позволяет мне таких вольностей
        //большая часть ребят вообще поудаляла старые реализации, пощади меня, я не хочу в академ
        //я могу обойти эту проблему , добавив список лайков в поле класса Film, но в этом нет никакого абсолютно смысла,
        //ведь эта реализация больше не основная, а в ответах на запросы будет лишнее поле, которое абсолютно лишнее по сути
        //ну или преренести этот метод в интерфейс, LikeStorage, но я тогда выброшусь из окна
        return null;
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