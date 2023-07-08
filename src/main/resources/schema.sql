DROP TABLE IF EXISTS STATUS;
DROP TABLE IF EXISTS GENRE;
DROP TABLE IF EXISTS MPA;


CREATE TABLE STATUS
(
    STATUS_ID INTEGER NOT NULL,
    STATUS    VARCHAR(300),
    PRIMARY KEY (STATUS_ID)
);

CREATE TABLE GENRE
(
    GENRE_ID   INTEGER      NOT NULL,
    GENRE_NAME VARCHAR(300) NOT NULL,
    PRIMARY KEY (GENRE_ID)
);

CREATE TABLE MPA
(
    MPA_ID   INTEGER      NOT NULL,
    MPA VARCHAR(300) NOT NULL,
    PRIMARY KEY (MPA_ID)
);

CREATE TABLE IF NOT EXISTS USERS
(
    ID       INTEGER AUTO_INCREMENT,
    NAME     VARCHAR(300) NOT NULL,
    LOGIN    VARCHAR(300) NOT NULL,
    EMAIL    VARCHAR(300) NOT NULL,
    BIRTHDAY DATE         NOT NULL,
    PRIMARY KEY (ID)
);


CREATE TABLE IF NOT EXISTS FRIENDS
(
    ID_USER                INTEGER,
    ID_FRIEND              INTEGER,
    REQUEST_FOR_FRIENDSHIP VARCHAR(100),
    STATUS_ID              INTEGER
);

CREATE TABLE IF NOT EXISTS FILMS
(
    ID           INTEGER AUTO_INCREMENT,
    NAME         VARCHAR(300) NOT NULL,
    DESCRIPTION  VARCHAR(200) NOT NULL,
    RELEASE_DATE DATE         NOT NULL,
    DURATION     INTEGER,
    MPA_ID       INTEGER,
    RATE         INTEGER,
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS FILM_GENRES
(
    FILM_ID  INTEGER NOT NULL,
    GENRE_ID INTEGER NOT NULL
);







