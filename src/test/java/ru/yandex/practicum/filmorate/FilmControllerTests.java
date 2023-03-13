package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

public class FilmControllerTests {

    Film film1;
    Film film2;
    FilmController controller;
    Validator validator;

    @BeforeEach
    public void beforeEach() {
        controller = new FilmController();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // Film Validation Tests

    @Test
    public void nullNameValidationTest() {
        film1 = Film.builder().duration(100).releaseDate(LocalDate.now()).description("Film Description").build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film1);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void emptyNameValidationTest() {
        film1 = Film.builder().name(" ").duration(100).releaseDate(LocalDate.now()).description("Film Description").build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film1);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void descriptionLengthMoreThan200ValidationTest() {
        String description = "a".repeat(201);
        film1 = Film.builder().name("Film Name").releaseDate(LocalDate.now()).duration(100).description(description).build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film1);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void zeroDurationValidationTest() {
        film1 = Film.builder().name("Film Name").description("Film description").releaseDate(LocalDate.now()).duration(0).build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film1);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void negativeDurationValidationTest() {
        film1 = Film.builder().name("Film Name").description("Film description").releaseDate(LocalDate.now()).duration(-1).build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film1);
        assertFalse(violations.isEmpty());
    }

    // Method addFilm() Tests

    @Test
    public void addFilmTest() {
        film1 = Film.builder().name("Test Name").duration(100).releaseDate(LocalDate.now()).description("Film Description").build();
        controller.addFilm(film1);
        assertEquals(1, controller.getFilms().size());
        assertEquals(film1, controller.getFilms().get(film1.getId()));
    }

    @Test
    public void invalidReleaseDateAddFilmTest() {
        film1 = Film.builder().name("Film Name").description("Film description").duration(100).releaseDate(LocalDate.of(1895, 12,27)).build();
        ValidationException e = assertThrows(ValidationException.class, ()-> controller.addFilm(film1));
        assertEquals("Дата публикации не может быть раньше 28.12.1895.", e.getMessage());
    }

    // Method updateFilm() Tests

    @Test
    public void updateFilmTest() {
        film1 = Film.builder().name("Test Name 1").duration(100).releaseDate(LocalDate.now()).description("Film Description 1").build();
        controller.addFilm(film1);
        assertEquals(1, controller.getFilms().size());

        film2 = Film.builder().id(film1.getId()).name("Test Name 2").duration(100).releaseDate(LocalDate.now()).description("Film Description 2").build();
        controller.addFilm(film2);
        assertEquals(film2, controller.getFilms().get(film2.getId()));
    }

    @Test
    public void invalidReleaseDateUpdateFilmTest() {
        film1 = Film.builder().name("Film Name 1").description("Film description 1").duration(100).releaseDate(LocalDate.of(1895, 12,28)).build();
        controller.addFilm(film1);
        film2 = Film.builder().id(film1.getId()).name("Film Name 2").description("Film description 2").duration(100).releaseDate(LocalDate.of(1895, 12,27)).build();
        ValidationException e = assertThrows(ValidationException.class, ()-> controller.updateFilm(film2));
        assertEquals("Дата публикации не может быть раньше 28.12.1895.", e.getMessage());
    }

    // Method getAllFilms() Tests

    @Test
    public void getAllFilmsTest() {
        film1 = Film.builder().name("Film Name 1").description("Film description 1").duration(100).releaseDate(LocalDate.of(1895, 12,28)).build();
        film2 = Film.builder().name("Film Name 2").description("Film description 2").duration(100).releaseDate(LocalDate.of(1895, 12,28)).build();
        controller.addFilm(film1);
        controller.addFilm(film2);
        ArrayList<Film> filmList = new ArrayList<>();
        filmList.add(film1);
        filmList.add(film2);
        assertEquals(filmList, controller.getAllFilms());
    }
}
