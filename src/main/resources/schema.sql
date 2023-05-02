CREATE TABLE IF NOT EXISTS ratings_mpa
(
    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS films
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR NOT NULL,
    description VARCHAR,
    release_date DATE NOT NULL,
    duration INT NOT NULL,
    rating_id INT NOT NULL REFERENCES ratings_mpa (id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS genres
(
    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS film_genres
(
    film_id BIGINT REFERENCES films (id) ON DELETE CASCADE,
    genre_id INT REFERENCES genres (id) ON DELETE RESTRICT,
    PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS users
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email VARCHAR NOT NULL,
    login VARCHAR NOT NULL,
    name VARCHAR,
    birthday DATE
);

CREATE TABLE IF NOT EXISTS film_likes
(
    film_id BIGINT REFERENCES films (id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, user_id)
);

CREATE TABLE IF NOT EXISTS friends
(
    user_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    friend_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    status BOOLEAN
);