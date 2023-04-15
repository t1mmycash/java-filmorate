create table if not exists public.users
(
    user_id integer unique primary key,
    email varchar(64),
    login varchar(64),
    name varchar(64),
    birthday date
);

create table if not exists public.friends
(
    user_id integer,
    friend_id integer,
    is_accepted character varying,
    constraint fk_friends_user_id foreign key (user_id) references public.users (user_id),
    constraint fk_friends_friend_id foreign key (friend_id) references public.users (user_id)
);

create table if not exists public.ratings
(
    rating_id integer unique primary key,
    rating_name varchar(8)
);

create table if not exists public.films
(
    film_id integer unique primary key,
    name varchar(64),
    description varchar(255),
    release_date date,
    duration integer,
    rating_id integer,
    constraint fk_films_rating_id foreign key (rating_id) references public.ratings (rating_id)
);

create table if not exists public.likes
(
    film_id integer,
    user_id integer,
    constraint fk_likes_film_id foreign key (film_id) references public.films (film_id),
    constraint fk_likes_user_id foreign key (user_id) references public.users (user_id)
);

create table if not exists public.genres
(
    genre_id integer unique primary key,
    genre_name varchar(32)
);

create table if not exists public.film_genres
(
    film_id integer,
    genre_id integer,
    constraint fk_genres_film_id foreign key (film_id) references public.films (film_id),
    constraint fk_genres_genre_id foreign key (genre_id) references public.genres (genre_id)
);







