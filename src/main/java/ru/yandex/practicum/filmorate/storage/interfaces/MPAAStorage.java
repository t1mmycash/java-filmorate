package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.MPAA;

import java.util.List;

public interface MPAAStorage {

    public MPAA getRatingById(int id);

    public List<MPAA> getAllRatings();
}
