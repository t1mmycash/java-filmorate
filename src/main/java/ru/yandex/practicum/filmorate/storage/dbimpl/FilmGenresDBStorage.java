package ru.yandex.practicum.filmorate.storage.dbimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmGenresStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FilmGenresDBStorage implements FilmGenresStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void updateFilmGenres(Film film) {
        String sql = "delete from public.film_genres where film_id = ?";
        jdbcTemplate.update(sql, film.getId());
        addAllFilmGenres(film);
    }

    @Override
    public void addAllFilmGenres(Film film) {
        String sql = "insert into public.film_genres (film_id, genre_id) values (?, ?)";
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(sql, film.getId(), genre.getId());
        }
    }

    @Override
    public List<Genre> getFilmGenres(int id) {
        String sql = "select fg.genre_id as id, g.genre_name as name " +
                "from public.film_genres as fg " +
                "join public.genres as g on fg.genre_id = g.genre_id " +
                "where film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), id);
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("id"), rs.getString("name"));
    }


}
