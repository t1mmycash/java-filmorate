package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private int id = 0;


    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            String message = "Дата публикации не может быть раньше 28.12.1895.";
            log.error(message);
            throw new ValidationException(message);
        }
        film.setId(newId());
        films.put(film.getId(), film);
        log.debug("Новый фильм:\n" + film.toString());
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            String message = "Дата публикации не может быть раньше 28.12.1895.";
            log.error(message);
            throw new ValidationException(message);
        }
        log.debug("Фильм:\n{}\nЗаменен на:{}", films.get(film.getId()).toString(), film.toString());
        films.remove(film.getId());
        films.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public ArrayList<Film> getAllFilms() {
        return new ArrayList<Film>(films.values());
    }

    public HashMap<Integer, Film> getFilms() {
        return films;
    }

    private int newId() {
        id += 1;
        return id;
    }

}
