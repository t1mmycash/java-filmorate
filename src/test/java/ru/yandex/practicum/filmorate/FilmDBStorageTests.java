package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPAA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dbimpl.FilmDBStorage;
import ru.yandex.practicum.filmorate.storage.dbimpl.UserDBStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.LikeStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDBStorageTests {
    private final JdbcTemplate jdbcTemplate;
    private final FilmDBStorage filmStorage;
    private final UserDBStorage userStorage;
    private final LikeStorage likeStorage;

    @Test
    public void addFilmTest() {
        Optional<Film> film = Optional.of(filmStorage.addFilm(Film.builder()
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.now().minusYears(2))
                .duration(90)
                .mpa(MPAA.builder().id(1).build())
                .build()));
        assertThat(film)
                .isPresent()
                .hasValueSatisfying(f -> assertThat(f).hasFieldOrPropertyWithValue("name", "name")
                ).hasValueSatisfying(f -> assertThat(f).hasFieldOrPropertyWithValue("description", "desc")
                ).hasValueSatisfying(f -> assertThat(f).hasFieldOrPropertyWithValue("duration", 90)
                );
    }

    @Test
    public void testGetFilmById() {

        Optional<Film> userOptional = Optional.of(filmStorage.getFilmById(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testGetAllFilms() {

        List<Film> films = filmStorage.getAllFilms();

        assertThat(films).isNotNull().hasSize(1);
    }

    @Test
    public void testUpdateFilm() {
        Film film = Film.builder()
                .name("upd")
                .description("upd")
                .releaseDate(LocalDate.now().minusYears(2))
                .duration(80)
                .mpa(MPAA.builder().id(1).build())
                .build();
        film.setId(1);
        filmStorage.updateFilm(film);
        Optional<Film> film2 = Optional.of(filmStorage.getFilmById(1));
        assertThat(film2)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("id", 1)

                ).hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("name", "upd")
                ).hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("description", "upd")
                ).hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("duration", 80)
                );
    }

    @Test
    public void addLikeFilm() {
        userStorage.addUser(User.builder()
                .email("mail@mail.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.now().minusYears(10))
                .build());
        likeStorage.like(1, 1);
        Assertions.assertEquals((jdbcTemplate.query("select user_id from likes where film_id = 1;", new SingleColumnRowMapper<>(Integer.class)).get(0)), 1);

    }

    @Test
    public void removeLike() {
        userStorage.addUser(User.builder()
                .email("mail@mail.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.now().minusYears(10))
                .build());

        likeStorage.removeLike(1, 1);


    }
}
