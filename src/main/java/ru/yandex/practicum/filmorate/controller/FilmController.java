package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }


    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            String message = "Дата публикации не может быть раньше 28.12.1895.";
            log.error(message);
            throw new ValidationException(message);
        }
        return filmStorage.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            String message = "Дата публикации не может быть раньше 28.12.1895.";
            log.error(message);
            throw new ValidationException(message);
        }
        return filmStorage.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public String likeTheFilm(@PathVariable Long id, @PathVariable Long userId) {
        checkId(id);
        checkId(userId);
        return filmService.likeTheFilm(id, userId);
    }

    @GetMapping
    public ArrayList<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        checkId(id);
        return filmStorage.getFilmById(id);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(required = false, defaultValue = "10") Long count) {
        return filmService.getPopularFilms(count);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public String removeLike(@PathVariable Long id, @PathVariable Long userId) {
        checkId(id);
        checkId(userId);
        return filmService.removeLike(id, userId);
    }

    private void checkId(Long id) {
        if(id == null) {
            throw new ValidationException("id не может быть null");
        }
    }

}
