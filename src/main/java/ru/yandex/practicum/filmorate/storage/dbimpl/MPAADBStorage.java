package ru.yandex.practicum.filmorate.storage.dbimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.MPAANotFoundException;
import ru.yandex.practicum.filmorate.model.MPAA;
import ru.yandex.practicum.filmorate.storage.interfaces.MPAAStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MPAADBStorage implements MPAAStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public MPAA getRatingById(int id) {
        String sql = "select * from public.ratings where rating_id = ?";
        List<MPAA> result = jdbcTemplate.query(sql, (rs, rowNum) -> makeMPAA(rs), id);
        if (result.isEmpty()) {
            throw new MPAANotFoundException("Неизвестный идентификатор рейтинга");
        }
        return result.get(0);
    }

    @Override
    public List<MPAA> getAllRatings() {
        String sql = "select * from public.ratings";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMPAA(rs));
    }

    private MPAA makeMPAA(ResultSet rs) throws SQLException {
        return new MPAA(rs.getInt("rating_id"), rs.getString("rating_name"));
    }
}
