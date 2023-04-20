package ru.yandex.practicum.filmorate.storage.dbimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreDBStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenreById(int id) {
        String sql = "select * from public.genres where genre_id = ?";
        List<Genre> result = jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), id);
        if (result.isEmpty()) {
            throw new GenreNotFoundException("Неизвестный идентификатор жанра");
        }
        return result.get(0);
    }

    @Override
    public List<Genre> getAllGenres() {
        String sql = "select * from public.genres";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
    }
}
