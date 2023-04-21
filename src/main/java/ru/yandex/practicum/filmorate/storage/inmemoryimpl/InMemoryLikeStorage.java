package ru.yandex.practicum.filmorate.storage.inmemoryimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.LikeStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("InMemoryLikeStorage")
public class InMemoryLikeStorage implements LikeStorage {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    private final HashMap<Integer, ArrayList<Integer>> filmLikes = new HashMap<>();

    @Autowired
    public InMemoryLikeStorage(@Qualifier("InMemoryFilmStorage") FilmStorage filmStorage, @Qualifier("InMemoryUserStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public String like(int filmId, int userId) {
        filmStorage.filmExistenceCheck(filmId);
        userStorage.userExistenceCheck(userId);
        if (!filmLikes.containsKey(filmId)) {
            filmLikes.put(filmId, new ArrayList<>());
        }
        filmLikes.get(filmId).add(userId);
        return "Лайк добавлен";
    }

    @Override
    public String removeLike(int filmId, int userId) {
        filmStorage.filmExistenceCheck(filmId);
        userStorage.userExistenceCheck(userId);
        filmLikes.get(filmId).remove((Integer) userId);
        return "Лайк успешно удален";
    }

    public List<ArrayList<Integer>> getMostLikedFilms() {
        List<ArrayList<Integer>> result = new ArrayList<>();
        for (Map.Entry<Integer, ArrayList<Integer>> film : filmLikes.entrySet()) {
            ArrayList<Integer> likes = new ArrayList<>();
            likes.add(film.getKey());
            result.add(film.getValue());
        }
        return result;
    }


}
