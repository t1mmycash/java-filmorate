merge into public.genres (genre_id, genre_name)
    values  (1, 'Комедия'),
            (2, 'Драма'),
            (3, 'Мультфильм'),
            (4, 'Триллер'),
            (5, 'Документальный'),
            (6, 'Боевик');

merge into public.ratings (rating_id, rating_name)
    values  (1, 'G'),
            (2, 'PG'),
            (3, 'PG-13'),
            (4, 'R'),
            (5, 'NC-17');