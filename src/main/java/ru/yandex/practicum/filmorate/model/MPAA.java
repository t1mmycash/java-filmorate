package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MPAA {
    private int id;
    private String name;

    public MPAA(int id, String name) {
        this.id = id;
        this.name = name;
    }

}
