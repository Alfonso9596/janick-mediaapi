DROP TABLE IF EXISTS game_platform_xref;
DROP TABLE IF EXISTS game_platform;
DROP TABLE IF EXISTS game_genre_xref;
DROP TABLE IF EXISTS game_rating;
DROP TABLE IF EXISTS game_genre;
DROP TABLE IF EXISTS game;
DROP TABLE IF EXISTS series_genre_xref;
DROP TABLE IF EXISTS series_rating;
DROP TABLE IF EXISTS movie_genre_xref;
DROP TABLE IF EXISTS movie_rating;
DROP TABLE IF EXISTS series;
DROP TABLE IF EXISTS movie;
DROP TABLE IF EXISTS movie_genre;
DROP TABLE IF EXISTS refreshtoken;
DROP TABLE IF EXISTS refreshtoken_seq;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS users;

--
-- Table structure for table users
--
CREATE TABLE users (
	ID bigint AUTO_INCREMENT,
	USERNAME VARCHAR(255) NOT NULL,
	PASSWORD VARCHAR(255) NOT NULL,
	PRIMARY KEY (ID)
);

--
-- Table structure for table refreshtoken
--
CREATE TABLE refreshtoken (
    ID bigint NOT NULL,
    EXPIRY_DATE datetime(6) NOT NULL,
    TOKEN VARCHAR(255) NOT NULL,
    USER_ID bigint DEFAULT NULL,
    PRIMARY KEY (ID),
    UNIQUE KEY UK_REFRESHTOKEN_TOKEN (TOKEN),
    UNIQUE KEY UK_REFRESHTOKEN_USER_ID (USER_ID),
    FOREIGN KEY (USER_ID) REFERENCES users (ID)
);

--
-- Table structure for table refreshtoken_seq
--
CREATE TABLE refreshtoken_seq (
    NEXT_VAL bigint DEFAULT NULL
);

--
-- Table structure for table roles
--
CREATE TABLE roles (
    ID bigint NOT NULL AUTO_INCREMENT,
    NAME VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (ID)
);

--
-- Table structure for table users_roles_xref
--
CREATE TABLE users_roles_xref (
    USER_ID bigint NOT NULL,
    ROLE_ID bigint NOT NULL,
    PRIMARY KEY (USER_ID,ROLE_ID),
    FOREIGN KEY (USER_ID) REFERENCES users (ID),
    FOREIGN KEY (ROLE_ID) REFERENCES roles (ID)
);

--
-- Table structure for table movie
--
CREATE TABLE movie (
	ID bigint AUTO_INCREMENT,
	NAME VARCHAR(255) NOT NULL,
	DESCRIPTION TEXT,
	YEAR VARCHAR(4),
	LENGTH INT,
	POSTER_FILEPATH VARCHAR(255),
	PRIMARY KEY (ID)
);

--
-- Table structure for table movie_genre
--
CREATE TABLE movie_genre (
	ID bigint AUTO_INCREMENT,
	NAME VARCHAR(255) NOT NULL,
	PRIMARY KEY (ID)
);

--
-- Table structure for table movie_genre_xref
--
CREATE TABLE movie_genre_xref (
	ID bigint AUTO_INCREMENT,
	MOVIE_ID bigint NOT NULL,
	GENRE_ID bigint NOT NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (MOVIE_ID) REFERENCES movie (ID),
	FOREIGN KEY (GENRE_ID) REFERENCES movie_genre (ID)
);

--
-- Table structure for table movie_rating
--
CREATE TABLE movie_rating (
	ID bigint AUTO_INCREMENT,
	MOVIE_ID bigint NOT NULL,
	RATING INT NOT NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (MOVIE_ID) REFERENCES movie (ID)
);

--
-- Table structure for table series
--
CREATE TABLE series (
	ID bigint AUTO_INCREMENT,
	NAME VARCHAR(255) NOT NULL,
	DESCRIPTION TEXT,
	YEAR_START VARCHAR(4),
	YEAR_END VARCHAR(4),
	EPISODE_LENGTH INT,
	POSTER_FILEPATH VARCHAR(255),
	PRIMARY KEY (ID)
);

--
-- Table structure for table series_genre_xref
--
CREATE TABLE series_genre_xref (
    ID bigint AUTO_INCREMENT,
    SERIES_ID bigint NOT NULL,
    GENRE_ID bigint NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (SERIES_ID) REFERENCES series (ID),
    FOREIGN KEY (GENRE_ID) REFERENCES movie_genre(ID)
);

--
-- Table structure for table series_rating
--
CREATE TABLE series_rating (
    ID bigint AUTO_INCREMENT,
    SERIES_ID bigint NOT NULL,
    RATING INT NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (SERIES_ID) REFERENCES series (ID)
);

--
-- Table structure for table game
--
CREATE TABLE game (
    ID bigint AUTO_INCREMENT,
    NAME VARCHAR(255) NOT NULL,
    DESCRIPTION TEXT,
    YEAR VARCHAR(4),
    POSTER_FILEPATH VARCHAR(255),
    PRIMARY KEY (ID)
);

--
-- Table structure for table game_genre
--
CREATE TABLE game_genre (
    ID bigint AUTO_INCREMENT,
	NAME VARCHAR(255) NOT NULL,
	PRIMARY KEY (ID)
);

--
-- Table structure for table game_genre_xref
--
CREATE TABLE game_genre_xref (
    ID bigint AUTO_INCREMENT,
	GAME_ID bigint NOT NULL,
	GENRE_ID bigint NOT NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (GAME_ID) REFERENCES game (ID),
	FOREIGN KEY (GENRE_ID) REFERENCES game_genre (ID)
);

--
-- Table structure for table game_rating
--
CREATE TABLE game_rating (
    ID bigint AUTO_INCREMENT,
    GAME_ID bigint NOT NULL,
    RATING INT NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (GAME_ID) REFERENCES game (ID)
);

--
-- Table structure for table game_platform
--
CREATE TABLE game_platform (
    ID bigint AUTO_INCREMENT,
    NAME VARCHAR(255) NOT NULL,
    PRIMARY KEY (ID)
);

--
-- Table structure for table game_platform_xref
--
CREATE TABLE game_platform_xref (
    ID bigint AUTO_INCREMENT,
    GAME_ID bigint NOT NULL,
    PLATFORM_ID bigint NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (GAME_ID) REFERENCES game (ID),
    FOREIGN KEY (PLATFORM_ID) REFERENCES game_platform (ID)
);