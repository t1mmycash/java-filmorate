package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }


    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            String message = "Дата публикации не может быть раньше 28.12.1895.";
            log.error(message);
            throw new ValidationException(message);
        }
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            String message = "Дата публикации не может быть раньше 28.12.1895.";
            log.error(message);
            throw new ValidationException(message);
        }
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public String like(@PathVariable Integer id, @PathVariable Integer userId) {
        checkId(id);
        checkId(userId);
        return filmService.like(id, userId);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        checkId(id);
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false, defaultValue = "10") Integer count) {
        return filmService.getPopularFilms(count);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public String removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
        checkId(id);
        checkId(userId);
        return filmService.removeLike(id, userId);
    }

    private void checkId(Integer id) {
        if (id == null) {
            throw new ValidationException("id не может быть null");
        }
    }

}
