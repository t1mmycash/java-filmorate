package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPAA;
import ru.yandex.practicum.filmorate.storage.dbimpl.MPAADBStorage;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MPAAController {
    private final MPAADBStorage mpaaDbStorage;

    @GetMapping("/{id}")
    public MPAA getRatingById(@PathVariable Integer id) {
        return mpaaDbStorage.getRatingById(id);
    }

    @GetMapping
    public List<MPAA> getAllRatings() {
        return mpaaDbStorage.getAllRatings();
    }

}
