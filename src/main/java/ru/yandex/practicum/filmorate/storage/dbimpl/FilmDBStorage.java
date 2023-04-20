package ru.yandex.practicum.filmorate.storage.dbimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmGenresStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.MPAAStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("FilmDBStorage")
@RequiredArgsConstructor
@Slf4j
public class FilmDBStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmGenresStorage filmGenresStorage;
    private final MPAAStorage mpaaStorage;
    private int id = 0;

    @Override
    public Film addFilm(Film film) {
        film.setId(newId());
        String sql = "insert into public.films (film_id, name, description, release_date, duration, rating_id) " +
                "values(?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "update films set name = ?, description = ?," +
                "release_date = ?, duration = ?, rating_id = ? where film_id = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        String sql = "select * from public.films where film_id = ?";
        List<Film> queryResult = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), id);
        if (queryResult.isEmpty()) {
            throw new FilmNotFoundException("Фильм с id = " + id + " не найден");
        }
        return queryResult.get(0);
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "select * from public.films";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String sql = "SELECT f.FILM_ID, " +
                "f.NAME, " +
                "f.DESCRIPTION, " +
                "f.RELEASE_DATE, " +
                "f.DURATION, " +
                "f.RATING_ID, " +
                "LIKES_COUNT " +
                "FROM public.FILMS f " +
                "LEFT JOIN (SELECT " +
                "FILM_ID, " +
                "count(user_id) AS likes_count " +
                "FROM public.LIKES " +
                "GROUP BY FILM_ID) AS l " +
                "ON f.FILM_ID = l.FILM_ID " +
                "ORDER BY LIKES_COUNT DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), count);
    }

    @Override
    public void filmExistenceCheck(int id) {
        String sql = "select film_id from public.films where film_id = ?";
        if (jdbcTemplate.query(sql, new SingleColumnRowMapper<>(Integer.class), id).isEmpty()) {
            throw new FilmNotFoundException("Фильм с id = " + id + " не найден");
        }
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Film film = Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(mpaaStorage.getRatingById(rs.getInt("rating_id")))
                .build();
        film.setGenres(filmGenresStorage.getFilmGenres(film.getId()));
        return film;
    }

    private int newId() {
        id += 1;
        return id;
    }
}
