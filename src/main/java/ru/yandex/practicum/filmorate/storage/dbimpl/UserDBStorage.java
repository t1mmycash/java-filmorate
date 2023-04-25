package ru.yandex.practicum.filmorate.storage.dbimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("UserDBStorage")
@Primary
@RequiredArgsConstructor
public class UserDBStorage implements UserStorage {

    private int id = 0;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User addUser(User user) {
        user.setId(newId());
        userNameCheck(user);
        String sql = "insert into public.users (user_id, email, login, name, birthday)" +
                "values(?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        return user;
    }

    @Override
    public User updateUser(User user) {
        userNameCheck(user);
        String sql = "update public.users set email = ?, login = ?, " +
                "name = ?, birthday = ? where user_id = ?";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public User getUserById(int id) {
        String sql = "select * from public.users where user_id = ?";
        List<User> queryResult = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id);
        if (queryResult.isEmpty()) {
            throw new UserNotFoundException("Пользователь с id = " + id + " не найден");
        }
        return queryResult.get(0);
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "select * from public.users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public void userExistenceCheck(int id) {
        String sql = "select user_id from public.users where user_id = ?";
        if (jdbcTemplate.query(sql, new SingleColumnRowMapper<>(Integer.class), id).isEmpty()) {
            throw new UserNotFoundException("Пользователь с id = " + id + " не найден");
        }
    }

    private User makeUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getInt("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }

    private void userNameCheck(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private int newId() {
        id += 1;
        return id;
    }
}
