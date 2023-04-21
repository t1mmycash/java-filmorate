package ru.yandex.practicum.filmorate.storage.dbimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;
import ru.yandex.practicum.filmorate.storage.interfaces.LikeStorage;

@Component("LikeDBStorage")
@RequiredArgsConstructor
public class LikeDBStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public String like(int filmId, int userId) {
        String sql = "insert into public.likes (film_id, user_id) values ( ?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
        return "Лайк успешно добавлен";
    }

    @Override
    public String removeLike(int filmId, int userId) {
        String sql = "delete from public.likes where film_id = ? and user_id = ?";
        int result = jdbcTemplate.update(sql, filmId, userId);
        if (result <= 0) {
            throw new LikeNotFoundException("Лайк не найден");
        }
        return "Лайк удален";
    }

}
