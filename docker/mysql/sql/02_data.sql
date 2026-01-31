--
-- Initial values for table roles
--
INSERT INTO roles (ID, NAME) VALUES (1, 'ADMIN');
INSERT INTO roles (ID, NAME) VALUES (2, 'USER');

--
-- Initial values for table refreshtoken_seq
--
INSERT INTO refreshtoken_seq VALUES (1);

--
-- Initial values for table users
--
INSERT INTO users (USERNAME, PASSWORD) VALUES ('admin', '$2a$10$PnY4vgD0A3/0KdX9uany6.8HVPOrois9PPPu3u9wOYMgrg2RG2mBG');
INSERT INTO users (USERNAME, PASSWORD) VALUES ('alfi', '$2a$10$PnY4vgD0A3/0KdX9uany6.8HVPOrois9PPPu3u9wOYMgrg2RG2mBG');

--
-- Initial values for table users_roles_xref
--
INSERT INTO users_roles_xref (USER_ID, ROLE_ID) VALUES (
    (SELECT ID FROM users WHERE USERNAME = 'admin'),
    (SELECT ID FROM roles WHERE NAME = 'ADMIN')
);
INSERT INTO users_roles_xref (USER_ID, ROLE_ID) VALUES (
    (SELECT ID FROM users WHERE USERNAME = 'alfi'),
    (SELECT ID FROM roles WHERE NAME = 'USER')
);
--
-- Initial values for table movie_genre
--
INSERT INTO movie_genre (ID, NAME) VALUES (1, 'Action');
INSERT INTO movie_genre (ID, NAME) VALUES (2, 'Abenteuer');
INSERT INTO movie_genre (ID, NAME) VALUES (3, 'Animationsfilm');
INSERT INTO movie_genre (ID, NAME) VALUES (4, 'Anime');
INSERT INTO movie_genre (ID, NAME) VALUES (5, 'Komödie');
INSERT INTO movie_genre (ID, NAME) VALUES (6, 'Kriminalität');
INSERT INTO movie_genre (ID, NAME) VALUES (7, 'Dokumentarfilm');
INSERT INTO movie_genre (ID, NAME) VALUES (8, 'Drama');
INSERT INTO movie_genre (ID, NAME) VALUES (9, 'Familie');
INSERT INTO movie_genre (ID, NAME) VALUES (10, 'Fantasy');
INSERT INTO movie_genre (ID, NAME) VALUES (11, 'Spielshow');
INSERT INTO movie_genre (ID, NAME) VALUES (12, 'Horror');
INSERT INTO movie_genre (ID, NAME) VALUES (13, 'Musik');
INSERT INTO movie_genre (ID, NAME) VALUES (14, 'Mysterium');
INSERT INTO movie_genre (ID, NAME) VALUES (15, 'Reality-TV');
INSERT INTO movie_genre (ID, NAME) VALUES (16, 'Romanze');
INSERT INTO movie_genre (ID, NAME) VALUES (17, 'Sci-Fi');
INSERT INTO movie_genre (ID, NAME) VALUES (18, 'Kurzfilm');
INSERT INTO movie_genre (ID, NAME) VALUES (19, 'Sport');
INSERT INTO movie_genre (ID, NAME) VALUES (20, 'Thriller');
INSERT INTO movie_genre (ID, NAME) VALUES (21, 'Western');
INSERT INTO movie_genre (ID, NAME) VALUES (22, 'Geschichte');
INSERT INTO movie_genre (ID, NAME) VALUES (23, 'Biographie');
INSERT INTO movie_genre (ID, NAME) VALUES (24, 'Krieg');
INSERT INTO movie_genre (ID, NAME) VALUES (25, 'Talk-Show');
INSERT INTO movie_genre (ID, NAME) VALUES (26, 'Gangster');
INSERT INTO movie_genre (ID, NAME) VALUES (27, 'Superheld');
INSERT INTO movie_genre (ID, NAME) VALUES (28, 'Kampfkünste');
INSERT INTO movie_genre (ID, NAME) VALUES (29, 'Cyberpunk');

--
-- Initial values for table game_genre
--
INSERT INTO game_genre (ID, NAME) VALUES (1, 'Abenteuer');
INSERT INTO game_genre (ID, NAME) VALUES (2, 'Action');
INSERT INTO game_genre (ID, NAME) VALUES (3, 'Arena');
INSERT INTO game_genre (ID, NAME) VALUES (4, 'Aufbau');
INSERT INTO game_genre (ID, NAME) VALUES (5, 'Battle Royale');
INSERT INTO game_genre (ID, NAME) VALUES (6, 'Beat em Up');
INSERT INTO game_genre (ID, NAME) VALUES (7, 'Brettspiel');
INSERT INTO game_genre (ID, NAME) VALUES (8, 'Clicker/Idle');
INSERT INTO game_genre (ID, NAME) VALUES (9, 'Crafting');
INSERT INTO game_genre (ID, NAME) VALUES (10, 'Dating-Sim');
INSERT INTO game_genre (ID, NAME) VALUES (11, 'Deckbuilding');
INSERT INTO game_genre (ID, NAME) VALUES (12, 'Escape Game');
INSERT INTO game_genre (ID, NAME) VALUES (13, 'Fighting Game');
INSERT INTO game_genre (ID, NAME) VALUES (14, 'Gesellschaftsspiel');
INSERT INTO game_genre (ID, NAME) VALUES (15, 'Hack and Slash');
INSERT INTO game_genre (ID, NAME) VALUES (16, 'Horror');
INSERT INTO game_genre (ID, NAME) VALUES (17, 'Indie');
INSERT INTO game_genre (ID, NAME) VALUES (18, 'Jump & Run');
INSERT INTO game_genre (ID, NAME) VALUES (19, 'Kriegspiel');
INSERT INTO game_genre (ID, NAME) VALUES (20, 'Laufsimulation');
INSERT INTO game_genre (ID, NAME) VALUES (21, 'Looter-Shooter');
INSERT INTO game_genre (ID, NAME) VALUES (22, 'Metroidvania');
INSERT INTO game_genre (ID, NAME) VALUES (23, 'MMO');
INSERT INTO game_genre (ID, NAME) VALUES (24, 'MOBA');
INSERT INTO game_genre (ID, NAME) VALUES (25, 'Mystery');
INSERT INTO game_genre (ID, NAME) VALUES (26, 'Open World');
INSERT INTO game_genre (ID, NAME) VALUES (27, 'Point & Click');
INSERT INTO game_genre (ID, NAME) VALUES (28, 'Puzzle');
INSERT INTO game_genre (ID, NAME) VALUES (29, 'Rennspiel');
INSERT INTO game_genre (ID, NAME) VALUES (30, 'Rhythmus');
INSERT INTO game_genre (ID, NAME) VALUES (31, 'Roguelike');
INSERT INTO game_genre (ID, NAME) VALUES (32, 'Roguelite');
INSERT INTO game_genre (ID, NAME) VALUES (33, 'Rollenspiel');
INSERT INTO game_genre (ID, NAME) VALUES (34, 'Sandbox');
INSERT INTO game_genre (ID, NAME) VALUES (35, 'Schleichspiel');
INSERT INTO game_genre (ID, NAME) VALUES (36, 'Shoot em up');
INSERT INTO game_genre (ID, NAME) VALUES (37, 'Shooter');
INSERT INTO game_genre (ID, NAME) VALUES (38, 'Simulation');
INSERT INTO game_genre (ID, NAME) VALUES (39, 'Soulslike');
INSERT INTO game_genre (ID, NAME) VALUES (40, 'Sport');
INSERT INTO game_genre (ID, NAME) VALUES (41, 'Strategie');
INSERT INTO game_genre (ID, NAME) VALUES (42, 'Survival');
INSERT INTO game_genre (ID, NAME) VALUES (43, 'Tower Defense');
INSERT INTO game_genre (ID, NAME) VALUES (44, 'Visual Novel');
INSERT INTO game_genre (ID, NAME) VALUES (45, 'Fantasy');
--
-- Initial values for table movie
--
INSERT INTO movie (ID, DESCRIPTION, LENGTH, NAME, YEAR, POSTER_FILEPATH) VALUES (
	1,
	'Ein Dieb stiehlt Unternehmensgeheimnisse mithilfe einer Technologie für gemeinsames Träumen. Dann erhält er den Auftrag, eine Idee im Kopf eines Geschäftsführers festzusetzen.',
	148,
	'Inception',
	2012,
	'movies/inception_2012/inception_2012.jpg'
);
INSERT INTO movie (ID, DESCRIPTION, LENGTH, NAME, YEAR, POSTER_FILEPATH) VALUES (
    2,
    'Der alternde Patriarch einer Verbrecherdynastie will die Herrschaft über sein geheimes Reich auf seinen widerwilligen Sohn übertragen.',
    175,
    'Der Pate',
    1972,
    'movies/der_pate_1972/der_pate_1972.jpg'
);
INSERT INTO movie (ID, DESCRIPTION, LENGTH, NAME, YEAR, POSTER_FILEPATH) VALUES (
    3,
    'Als der fiese Joker Verwüstung und Chaos über die Menschen in Gotham bringt, muss sich der "dunkle Ritter" einer der härtesten psychologischen Prüfungen seiner Fähigkeit, Unrecht zu bekämpfen, unterziehen.',
    152,
    'The Dark Knight',
    2008,
    'movies/the_dark_knight_2008/the_dark_knight_2008.jpg'
);
INSERT INTO movie (ID, DESCRIPTION, LENGTH, NAME, YEAR, POSTER_FILEPATH) VALUES (
    4,
    'Gandalf und Aragorn führen die Männer der Mittelerde in den Kampf gegen Saurons Armee, um ihn von Frodo und Sam abzulenken, die sich gerade dem Schicksalsberg mit dem Einen Ring nähern.',
    201,
    'Der Herr der Ringe: Die Rückkehr des Königs',
    2003,
    'movies/der_herr_der_ringe_die_rückkehr_des_königs_2003/der_herr_der_ringe_die_rückkehr_des_königs_2003.jpg'
);
INSERT INTO movie (ID, DESCRIPTION, LENGTH, NAME, YEAR, POSTER_FILEPATH) VALUES (
    5,
    'Im Zweiten Weltkrieg macht sich Oskar Schindler in dem von den Deutschen besetzten Polen zunehmend Sorgen um seine jüdischen Mitarbeiter, nachdem er Zeuge von deren Verfolgung durch die Nazis geworden ist.',
    195,
    'Schindlers Liste',
    1993,
    'movies/schindlers_liste_1993/schindlers_liste_1993.jpg'
);
INSERT INTO movie (ID, DESCRIPTION, LENGTH, NAME, YEAR, POSTER_FILEPATH) VALUES (
    6,
    'Die Leben zweier Auftragskiller, eines Boxers, einer Gangsterbraut und eines Pärchens, das Diner-Restaurants überfällt, werden in vier Episoden miteinander verwoben, die aus Gewalt und Erlösung bestehen.',
    214,
    'Pulp Fiction',
    1993,
    'movies/pulp_fiction_1993/pulp_fiction_1993.jpg'
);
INSERT INTO movie (ID, DESCRIPTION, LENGTH, NAME, YEAR, POSTER_FILEPATH) VALUES (
    7,
    'Forrest Gump, obgleich nicht sonderlich mit Intelligenz gesegnet, war doch durch Zufall bei vielen historischen Augenblicken zugegen; Jenny Curran, seine große Liebe, entzieht sich ihm jedoch.',
    144,
    'Forrest Gump',
    1994,
    'movies/forrest_gump_1994/forrest_gump_1994.jpg'
);
INSERT INTO movie (ID, DESCRIPTION, LENGTH, NAME, YEAR, POSTER_FILEPATH) VALUES (
    8,
    'Ein unter Schlaflosigkeit leidender Büroangestellter sucht nach einer Möglichkeit, sein Leben zu ändern, und trifft dabei auf einen sorglosen Seifenhändler, der im Untergrund einen Kampfclub unterhält, der sich als etwas noch viel Größeres herausstellt.',
    139,
    'Fight Club',
    1999,
    'movies/fight_club_1999/fight_club_1999.jpg'
);
INSERT INTO movie (ID, DESCRIPTION, LENGTH, NAME, YEAR, POSTER_FILEPATH) VALUES (
    9,
    'Ein Computerhacker erfährt von mysteriösen Rebellen die Wahrheit über seine Realität und seine Rolle im Krieg gegen deren Kontrolleure.',
    136,
    'Matrix',
    1999,
    'movies/matrix_1999/matrix_1999.jpg'
);
INSERT INTO movie (ID, DESCRIPTION, LENGTH, NAME, YEAR, POSTER_FILEPATH) VALUES (
    10,
    'Ein Forscherteam reist auf der Suche nach neuen Welten durch ein Wurmloch im Universum, um das Überleben der Menschheit sicherzustellen.',
    169,
    'Interstellar',
    2014,
    'movies/interstellar_2014/interstellar_2014.jpg'
);
INSERT INTO movie (ID, DESCRIPTION, LENGTH, NAME, YEAR, POSTER_FILEPATH) VALUES (
    11,
    'Zwei Kripobeamte, ein Neuling und ein altgedienter Fuchs jagen einen Serienmörder, der die sieben Todsünden als seinen Modus Operandi verwendet.',
    127,
    'Sieben',
    1995,
    'movies/sieben_1995/sieben_1995.jpg'
);

--
-- Initial values for table movie_genre_xref
--
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
	(SELECT ID FROM movie WHERE NAME = 'Inception'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Action')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
	(SELECT ID FROM movie WHERE NAME = 'Inception'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Abenteuer')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
	(SELECT ID FROM movie WHERE NAME = 'Inception'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Sci-Fi')
);

INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Der Pate'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Kriminalität')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Der Pate'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Drama')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Der Pate'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Gangster')
);

INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'The Dark Knight'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Thriller')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'The Dark Knight'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Kriminalität')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'The Dark Knight'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Drama')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'The Dark Knight'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Action')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'The Dark Knight'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Superheld')
);

INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Der Herr der Ringe: Die Rückkehr des Königs'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Drama')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Der Herr der Ringe: Die Rückkehr des Königs'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Abenteuer')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Der Herr der Ringe: Die Rückkehr des Königs'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Fantasy')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Der Herr der Ringe: Die Rückkehr des Königs'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Action')
);

INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Schindlers Liste'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Geschichte')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Schindlers Liste'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Drama')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Schindlers Liste'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Biographie')
);

INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Pulp Fiction'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Kriminalität')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Pulp Fiction'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Drama')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Pulp Fiction'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Gangster')
);

INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Forrest Gump'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Romanze')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Forrest Gump'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Drama')
);

INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Fight Club'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Drama')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Fight Club'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Thriller')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Fight Club'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Kriminalität')
);

INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Matrix'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Sci-Fi')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Matrix'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Action')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Matrix'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Kampfkünste')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Matrix'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Cyberpunk')
);

INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Interstellar'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Sci-Fi')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Interstellar'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Drama')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Interstellar'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Abenteuer')
);

INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Sieben'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Mysterium')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Sieben'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Kriminalität')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Sieben'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Drama')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Sieben'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Thriller')
);

--
-- Initial values for table movie_rating
--
INSERT INTO movie_rating (MOVIE_ID, USER_ID, RATING) VALUES (
	(SELECT ID FROM movie WHERE NAME = 'Inception'),
	(SELECT ID FROM users WHERE USERNAME = 'admin'),
	3
);
INSERT INTO movie_rating (MOVIE_ID, USER_ID, RATING) VALUES (
	(SELECT ID FROM movie WHERE NAME = 'Inception'),
	(SELECT ID FROM users WHERE USERNAME = 'alfi'),
	5
);

--
-- Initial values for table series
--
INSERT INTO series (ID, DESCRIPTION, EPISODE_LENGTH, NAME, YEAR_START, YEAR_END, POSTER_FILEPATH) VALUES (
	1,
	'Ein Highschool-Chemielehrer, bei dem ein inoperabler Lungenkrebs diagnostiziert wurde, wendet sich der Herstellung und dem Verkauf von Methamphetamin zu, um die Zukunft seiner Familie zu sichern.',
	45,
	'Breaking Bad',
	2008,
	2013,
	'series/breaking_bad_2008/breaking_bad_2008.jpg'
);

--
-- Initial values for table series_genre_xref
--
INSERT INTO series_genre_xref (SERIES_ID, GENRE_ID) VALUES (
	(SELECT ID FROM series WHERE NAME = 'Breaking Bad'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Drama')
);
INSERT INTO series_genre_xref (SERIES_ID, GENRE_ID) VALUES (
	(SELECT ID FROM series WHERE NAME = 'Breaking Bad'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Kriminalität')
);
INSERT INTO series_genre_xref (SERIES_ID, GENRE_ID) VALUES (
	(SELECT ID FROM series WHERE NAME = 'Breaking Bad'),
	(SELECT ID FROM movie_genre WHERE NAME = 'Thriller')
);

--
-- Initial values for table series_rating
--
INSERT INTO series_rating (SERIES_ID, USER_ID, RATING) VALUES (
	(SELECT ID FROM series WHERE NAME = 'Breaking Bad'),
	(SELECT ID FROM users WHERE USERNAME = 'admin'),
	3
);
INSERT INTO series_rating (SERIES_ID, USER_ID, RATING) VALUES (
	(SELECT ID FROM series WHERE NAME = 'Breaking Bad'),
	(SELECT ID FROM users WHERE USERNAME = 'alfi'),
	5
);

--
-- Initial values for table game
--
INSERT INTO game (ID, DESCRIPTION, NAME, YEAR, POSTER_FILEPATH) VALUES (
	1,
	'DAS NEUE FANTASY-ACTION-RPG. Erhebt Euch, Befleckter, und lasst Euch von der Gnade leiten, um die Macht des Eldenrings zu befehligen und zum Eldenfürsten im Zwischenland zu werden.',
	'Elden Ring',
	2022,
	'games/elden_ring_2022/elden_ring_2022.jpg'
);

--
-- Initial values for table game_genre_xref
--
INSERT INTO game_genre_xref (GAME_ID, GENRE_ID) VALUES (
	(SELECT ID FROM game WHERE NAME = 'Elden Ring'),
	(SELECT ID FROM game_genre WHERE NAME = 'Soulslike')
);
INSERT INTO game_genre_xref (GAME_ID, GENRE_ID) VALUES (
	(SELECT ID FROM game WHERE NAME = 'Elden Ring'),
	(SELECT ID FROM game_genre WHERE NAME = 'Action')
);
INSERT INTO game_genre_xref (GAME_ID, GENRE_ID) VALUES (
	(SELECT ID FROM game WHERE NAME = 'Elden Ring'),
	(SELECT ID FROM game_genre WHERE NAME = 'Fantasy')
);
INSERT INTO game_genre_xref (GAME_ID, GENRE_ID) VALUES (
	(SELECT ID FROM game WHERE NAME = 'Elden Ring'),
	(SELECT ID FROM game_genre WHERE NAME = 'Horror')
);

--
-- Initial values for table game_rating
--
INSERT INTO game_rating (GAME_ID, USER_ID, RATING) VALUES (
	(SELECT ID FROM game WHERE NAME = 'Elden Ring'),
	(SELECT ID FROM users WHERE USERNAME = 'admin'),
	4
);
INSERT INTO game_rating (GAME_ID, USER_ID, RATING) VALUES (
	(SELECT ID FROM game WHERE NAME = 'Elden Ring'),
	(SELECT ID FROM users WHERE USERNAME = 'alfi'),
	5
);

--
-- Initial values for table game_platform
--
INSERT INTO game_platform (ID, NAME) VALUES (1, 'PC');
INSERT INTO game_platform (ID, NAME) VALUES (2, 'PS5');
INSERT INTO game_platform (ID, NAME) VALUES (3, 'PS4');
INSERT INTO game_platform (ID, NAME) VALUES (4, 'PS3');
INSERT INTO game_platform (ID, NAME) VALUES (5, 'PS2');
INSERT INTO game_platform (ID, NAME) VALUES (6, 'PSP');
INSERT INTO game_platform (ID, NAME) VALUES (7, 'GB');
INSERT INTO game_platform (ID, NAME) VALUES (8, 'GBA');
INSERT INTO game_platform (ID, NAME) VALUES (9, 'GBC');
INSERT INTO game_platform (ID, NAME) VALUES (10, 'SNES');

--
-- Initial values for table game_platform_xref
--
INSERT INTO game_platform_xref (GAME_ID, PLATFORM_ID) VALUES (
    (SELECT ID FROM game WHERE NAME = 'Elden Ring'),
    1
);