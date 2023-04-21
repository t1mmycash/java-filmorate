package ru.yandex.practicum.filmorate.storage.dbimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("FriendDBStorage")
@RequiredArgsConstructor
public class FriendDBStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public String addFriend(int id, int friendId) {
        String isThereInTheDBSql = "select count(user_id) " +
                "from public.friends " +
                "where (user_id = ? and friend_id = ?) or (user_id = ? and friend_id = ?)";

        String insertSql = "insert into public.friends (user_id, friend_id, is_accepted) values(?, ?, ?)";

        String updateSql = "update public.friends set is_accepted = 'y' " +
                "where where user_id in (?, ?) and friend_id in (?, ?)";

        if (jdbcTemplate.query(isThereInTheDBSql, new SingleColumnRowMapper<>(Integer.class), id, friendId, friendId, id).get(0) == 0) {
            jdbcTemplate.update(insertSql, id, friendId, 'n');
        } else {
            jdbcTemplate.update(updateSql, id, friendId, friendId, id);
        }
        return "Друг добавлен";
    }

    @Override
    public String deleteFriend(int id, int friendId) {
        String deleteUnaccepted = "delete from public.friends " +
                "where (user_id = ? and friend_id = ?) " +
                "and is_accepted = 'n'";

        String updateIfIdEqualsUserId = "update public.friends set is_accepted = 'n' " +
                "where user_id = ? and friend_id = ?";

        String updateIfIdNotEqualsUserId = "update public.friends set is_accepted = 'n', user_id = ?, friend_id = ? " +
                "where user_id = ?, and friend_id = ?";
        if (jdbcTemplate.update(deleteUnaccepted, id, friendId) != 1) {
            if (jdbcTemplate.update(updateIfIdEqualsUserId, friendId, id) != 1) {
                jdbcTemplate.update(updateIfIdNotEqualsUserId, friendId, id, id, friendId);
            }
        }
        return "Друг удален";
    }

    @Override
    public List<User> getFriendsById(int id) {
        String sql = "SELECT * " +
                "FROM public.users AS u " +
                "JOIN (" +
                "SELECT friend_id AS id " +
                "FROM public.friends " +
                "WHERE user_id = ? " +
                "UNION " +
                "SELECT user_id AS id " +
                "FROM public.friends " +
                "WHERE is_accepted = 'y' AND friend_id = ?) AS i " +
                "ON u.user_id = i.id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id, id);
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        String sql = "SELECT * " +
                "FROM public.users AS u " +
                "JOIN ( " +
                "SELECT DISTINCT id " +
                "FROM ( " +
                "SELECT friend_id AS id  " +
                "FROM public.friends  " +
                "WHERE user_id IN (?, ?) AND friend_id NOT IN (?, ?) " +
                "UNION " +
                "SELECT user_id AS id " +
                "FROM public.friends  " +
                "WHERE friend_id in(?, ?) AND user_id NOT IN (?, ?) AND is_accepted = 'y' " +
                ")" +
                ") AS i " +
                "ON u.user_id = i.id";
        return jdbcTemplate.query(sql, (rs, romNum) -> makeUser(rs), id, otherId, id, otherId, id, otherId, id, otherId);
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
}
