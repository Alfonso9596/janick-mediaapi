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
INSERT INTO game_genre (ID, NAME) VALUES (13, 'Gesellschaftsspiel');
INSERT INTO game_genre (ID, NAME) VALUES (14, 'Hack and Slash');
INSERT INTO game_genre (ID, NAME) VALUES (15, 'Horror');
INSERT INTO game_genre (ID, NAME) VALUES (16, 'Indie');
INSERT INTO game_genre (ID, NAME) VALUES (17, 'Jump & Run');
INSERT INTO game_genre (ID, NAME) VALUES (18, 'Kampfspiel');
INSERT INTO game_genre (ID, NAME) VALUES (19, 'Kreaturensammler');
INSERT INTO game_genre (ID, NAME) VALUES (20, 'Kriegspiel');
INSERT INTO game_genre (ID, NAME) VALUES (21, 'Laufsimulation');
INSERT INTO game_genre (ID, NAME) VALUES (22, 'Looter-Shooter');
INSERT INTO game_genre (ID, NAME) VALUES (23, 'Metroidvania');
INSERT INTO game_genre (ID, NAME) VALUES (24, 'MMO');
INSERT INTO game_genre (ID, NAME) VALUES (25, 'MOBA');
INSERT INTO game_genre (ID, NAME) VALUES (26, 'Mystery');
INSERT INTO game_genre (ID, NAME) VALUES (27, 'Open World');
INSERT INTO game_genre (ID, NAME) VALUES (28, 'Point & Click');
INSERT INTO game_genre (ID, NAME) VALUES (29, 'Puzzle');
INSERT INTO game_genre (ID, NAME) VALUES (30, 'Rennspiel');
INSERT INTO game_genre (ID, NAME) VALUES (31, 'Rhythmus');
INSERT INTO game_genre (ID, NAME) VALUES (32, 'Roguelike');
INSERT INTO game_genre (ID, NAME) VALUES (33, 'Roguelite');
INSERT INTO game_genre (ID, NAME) VALUES (34, 'Rollenspiel');
INSERT INTO game_genre (ID, NAME) VALUES (35, 'Rundenbasiert');
INSERT INTO game_genre (ID, NAME) VALUES (36, 'Sandbox');
INSERT INTO game_genre (ID, NAME) VALUES (37, 'Schleichspiel');
INSERT INTO game_genre (ID, NAME) VALUES (38, 'Shoot em up');
INSERT INTO game_genre (ID, NAME) VALUES (39, 'Shooter');
INSERT INTO game_genre (ID, NAME) VALUES (40, 'Simulation');
INSERT INTO game_genre (ID, NAME) VALUES (41, 'Soulslike');
INSERT INTO game_genre (ID, NAME) VALUES (42, 'Sport');
INSERT INTO game_genre (ID, NAME) VALUES (43, 'Strategie');
INSERT INTO game_genre (ID, NAME) VALUES (44, 'Survival');
INSERT INTO game_genre (ID, NAME) VALUES (45, 'Tower Defense');
INSERT INTO game_genre (ID, NAME) VALUES (46, 'Visual Novel');
INSERT INTO game_genre (ID, NAME) VALUES (47, 'Fantasy');

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
INSERT INTO game_platform (ID, NAME) VALUES (11, 'Switch');
INSERT INTO game_platform (ID, NAME) VALUES (12, 'Switch 2');
INSERT INTO game_platform (ID, NAME) VALUES (13, 'XBOX360');
INSERT INTO game_platform (ID, NAME) VALUES (14, 'XBOX One');
INSERT INTO game_platform (ID, NAME) VALUES (15, 'XBOX X/S');

--
-- Initial values for table music_genre
--
INSERT INTO music_genre (ID, NAME) VALUES (1, 'Acoustic');
INSERT INTO music_genre (ID, NAME) VALUES (2, 'Alternative');
INSERT INTO music_genre (ID, NAME) VALUES (3, 'Ambient');
INSERT INTO music_genre (ID, NAME) VALUES (4, 'Americana');
INSERT INTO music_genre (ID, NAME) VALUES (5, 'Avant-Garde');
INSERT INTO music_genre (ID, NAME) VALUES (6, 'Baroque');
INSERT INTO music_genre (ID, NAME) VALUES (7, 'Bluegrass');
INSERT INTO music_genre (ID, NAME) VALUES (8, 'Blues');
INSERT INTO music_genre (ID, NAME) VALUES (9, 'Bollywood');
INSERT INTO music_genre (ID, NAME) VALUES (10, 'Bossa Nova');
INSERT INTO music_genre (ID, NAME) VALUES (11, 'Calypso');
INSERT INTO music_genre (ID, NAME) VALUES (12, 'Celtic');
INSERT INTO music_genre (ID, NAME) VALUES (13, 'Chamber');
INSERT INTO music_genre (ID, NAME) VALUES (14, 'Chiptune');
INSERT INTO music_genre (ID, NAME) VALUES (15, 'Classical');
INSERT INTO music_genre (ID, NAME) VALUES (16, 'Country');
INSERT INTO music_genre (ID, NAME) VALUES (17, 'Cumbia');
INSERT INTO music_genre (ID, NAME) VALUES (18, 'Dance');
INSERT INTO music_genre (ID, NAME) VALUES (19, 'Death Metal');
INSERT INTO music_genre (ID, NAME) VALUES (20, 'Disco');
INSERT INTO music_genre (ID, NAME) VALUES (21, 'Doo-Wop');
INSERT INTO music_genre (ID, NAME) VALUES (22, 'Drum and Bass');
INSERT INTO music_genre (ID, NAME) VALUES (23, 'Dub');
INSERT INTO music_genre (ID, NAME) VALUES (24, 'Dubstep');
INSERT INTO music_genre (ID, NAME) VALUES (25, 'Electronic');
INSERT INTO music_genre (ID, NAME) VALUES (26, 'Electronicore');
INSERT INTO music_genre (ID, NAME) VALUES (27, 'Emo');
INSERT INTO music_genre (ID, NAME) VALUES (28, 'Experimental');
INSERT INTO music_genre (ID, NAME) VALUES (29, 'Flamenco');
INSERT INTO music_genre (ID, NAME) VALUES (30, 'Folk');
INSERT INTO music_genre (ID, NAME) VALUES (31, 'Free Jazz');
INSERT INTO music_genre (ID, NAME) VALUES (32, 'Funk');
INSERT INTO music_genre (ID, NAME) VALUES (33, 'Garage Rock');
INSERT INTO music_genre (ID, NAME) VALUES (34, 'Gospel');
INSERT INTO music_genre (ID, NAME) VALUES (35, 'Grunge');
INSERT INTO music_genre (ID, NAME) VALUES (36, 'Gypsy Jazz');
INSERT INTO music_genre (ID, NAME) VALUES (37, 'Hajakuru');
INSERT INTO music_genre (ID, NAME) VALUES (38, 'Hard Bop');
INSERT INTO music_genre (ID, NAME) VALUES (39, 'Hard Rock');
INSERT INTO music_genre (ID, NAME) VALUES (40, 'Hardcore Punk');
INSERT INTO music_genre (ID, NAME) VALUES (41, 'Heavy Metal');
INSERT INTO music_genre (ID, NAME) VALUES (42, 'Hip-Hop');
INSERT INTO music_genre (ID, NAME) VALUES (43, 'Honky-Tonk');
INSERT INTO music_genre (ID, NAME) VALUES (44, 'Horrorpunk');
INSERT INTO music_genre (ID, NAME) VALUES (45, 'House');
INSERT INTO music_genre (ID, NAME) VALUES (46, 'Indie');
INSERT INTO music_genre (ID, NAME) VALUES (47, 'Industrial');
INSERT INTO music_genre (ID, NAME) VALUES (48, 'Jazz');
INSERT INTO music_genre (ID, NAME) VALUES (49, 'J-Pop');
INSERT INTO music_genre (ID, NAME) VALUES (50, 'K-Pop');
INSERT INTO music_genre (ID, NAME) VALUES (51, 'Latin');
INSERT INTO music_genre (ID, NAME) VALUES (52, 'Lo-Fi');
INSERT INTO music_genre (ID, NAME) VALUES (53, 'Lounge');
INSERT INTO music_genre (ID, NAME) VALUES (54, 'Mambo');
INSERT INTO music_genre (ID, NAME) VALUES (55, 'Mariachi');
INSERT INTO music_genre (ID, NAME) VALUES (56, 'Merengue');
INSERT INTO music_genre (ID, NAME) VALUES (57, 'Metalcore');
INSERT INTO music_genre (ID, NAME) VALUES (58, 'Motown');
INSERT INTO music_genre (ID, NAME) VALUES (59, 'New Age');
INSERT INTO music_genre (ID, NAME) VALUES (60, 'New Wave');
INSERT INTO music_genre (ID, NAME) VALUES (61, 'Noise');
INSERT INTO music_genre (ID, NAME) VALUES (62, 'Nu Metal');
INSERT INTO music_genre (ID, NAME) VALUES (63, 'Opera');
INSERT INTO music_genre (ID, NAME) VALUES (64, 'Orchestral');
INSERT INTO music_genre (ID, NAME) VALUES (65, 'Polka');
INSERT INTO music_genre (ID, NAME) VALUES (66, 'Pop');
INSERT INTO music_genre (ID, NAME) VALUES (67, 'Pop Punk');
INSERT INTO music_genre (ID, NAME) VALUES (68, 'Post Punk');
INSERT INTO music_genre (ID, NAME) VALUES (69, 'Progressive Rock');
INSERT INTO music_genre (ID, NAME) VALUES (70, 'Psychedelic');
INSERT INTO music_genre (ID, NAME) VALUES (71, 'Punk');
INSERT INTO music_genre (ID, NAME) VALUES (72, 'R&B');
INSERT INTO music_genre (ID, NAME) VALUES (73, 'Rap');
INSERT INTO music_genre (ID, NAME) VALUES (74, 'Reggae');
INSERT INTO music_genre (ID, NAME) VALUES (75, 'Reggaeton');
INSERT INTO music_genre (ID, NAME) VALUES (76, 'Rock');
INSERT INTO music_genre (ID, NAME) VALUES (77, 'Rockabilly');
INSERT INTO music_genre (ID, NAME) VALUES (78, 'Salsa');
INSERT INTO music_genre (ID, NAME) VALUES (79, 'Samba');
INSERT INTO music_genre (ID, NAME) VALUES (80, 'Sertanejo');
INSERT INTO music_genre (ID, NAME) VALUES (81, 'Shoegaze');
INSERT INTO music_genre (ID, NAME) VALUES (82, 'Ska');
INSERT INTO music_genre (ID, NAME) VALUES (83, 'Skiffle');
INSERT INTO music_genre (ID, NAME) VALUES (84, 'Soul');
INSERT INTO music_genre (ID, NAME) VALUES (85, 'Space Rock');
INSERT INTO music_genre (ID, NAME) VALUES (86, 'Stoner Rock');
INSERT INTO music_genre (ID, NAME) VALUES (87, 'Surf Rock');
INSERT INTO music_genre (ID, NAME) VALUES (88, 'Swing');
INSERT INTO music_genre (ID, NAME) VALUES (89, 'Swing Revival');
INSERT INTO music_genre (ID, NAME) VALUES (90, 'Synthpop');
INSERT INTO music_genre (ID, NAME) VALUES (91, 'Tango');
INSERT INTO music_genre (ID, NAME) VALUES (92, 'Techno');
INSERT INTO music_genre (ID, NAME) VALUES (93, 'Thrash Metal');
INSERT INTO music_genre (ID, NAME) VALUES (94, 'Trip-Hop');
INSERT INTO music_genre (ID, NAME) VALUES (95, 'Tropical');
INSERT INTO music_genre (ID, NAME) VALUES (96, 'Uplifting Trance');
INSERT INTO music_genre (ID, NAME) VALUES (97, 'Vocal Jazz');
INSERT INTO music_genre (ID, NAME) VALUES (98, 'World');
INSERT INTO music_genre (ID, NAME) VALUES (99, 'Worldbeat');
INSERT INTO music_genre (ID, NAME) VALUES (100, 'Yacht Rock');
INSERT INTO music_genre (ID, NAME) VALUES (101, 'Yodeling');
INSERT INTO music_genre (ID, NAME) VALUES (102, 'Zouk');
INSERT INTO music_genre (ID, NAME) VALUES (103, 'Zydeco');

--
-- Initial values for table movie
--
INSERT INTO movie (ID, DESCRIPTION, LENGTH, NAME, YEAR, POSTER_FILEPATH, CREATED_AT, LAST_UPDATED, USER_ID) VALUES (
	1,
	'Ein Dieb stiehlt Unternehmensgeheimnisse mithilfe einer Technologie für gemeinsames Träumen. Dann erhält er den Auftrag, eine Idee im Kopf eines Geschäftsführers festzusetzen.',
	148,
	'Inception',
	2012,
	'movies/inception_2012/inception_2012.jpg',
	'2026-02-07T15:24:53.365764',
	'2026-02-07T15:24:53.365764',
	1
);
INSERT INTO movie (ID, DESCRIPTION, LENGTH, NAME, YEAR, POSTER_FILEPATH, CREATED_AT, LAST_UPDATED, USER_ID) VALUES (
    2,
    'Der alternde Patriarch einer Verbrecherdynastie will die Herrschaft über sein geheimes Reich auf seinen widerwilligen Sohn übertragen.',
    175,
    'Der Pate',
    1972,
    'movies/der_pate_1972/der_pate_1972.jpg',
    '2026-02-07T15:24:53.365764',
    '2026-02-07T15:24:53.365764',
    1
);
INSERT INTO movie (ID, DESCRIPTION, LENGTH, NAME, YEAR, POSTER_FILEPATH, CREATED_AT, LAST_UPDATED, USER_ID) VALUES (
    3,
    'Als der fiese Joker Verwüstung und Chaos über die Menschen in Gotham bringt, muss sich der "dunkle Ritter" einer der härtesten psychologischen Prüfungen seiner Fähigkeit, Unrecht zu bekämpfen, unterziehen.',
    152,
    'The Dark Knight',
    2008,
    'movies/the_dark_knight_2008/the_dark_knight_2008.jpg',
    '2026-02-07T15:24:53.365764',
    '2026-02-07T15:24:53.365764',
    1
);
INSERT INTO movie (ID, DESCRIPTION, LENGTH, NAME, YEAR, POSTER_FILEPATH, CREATED_AT, LAST_UPDATED, USER_ID) VALUES (
    4,
    'Gandalf und Aragorn führen die Männer der Mittelerde in den Kampf gegen Saurons Armee, um ihn von Frodo und Sam abzulenken, die sich gerade dem Schicksalsberg mit dem Einen Ring nähern.',
    201,
    'Der Herr der Ringe: Die Rückkehr des Königs',
    2003,
    'movies/der_herr_der_ringe_die_rückkehr_des_königs_2003/der_herr_der_ringe_die_rückkehr_des_königs_2003.jpg',
    '2026-02-07T15:24:53.365764',
    '2026-02-07T15:24:53.365764',
    1
);
INSERT INTO movie (ID, DESCRIPTION, LENGTH, NAME, YEAR, POSTER_FILEPATH, CREATED_AT, LAST_UPDATED, USER_ID) VALUES (
    5,
    'Im Zweiten Weltkrieg macht sich Oskar Schindler in dem von den Deutschen besetzten Polen zunehmend Sorgen um seine jüdischen Mitarbeiter, nachdem er Zeuge von deren Verfolgung durch die Nazis geworden ist.',
    195,
    'Schindlers Liste',
    1993,
    'movies/schindlers_liste_1993/schindlers_liste_1993.jpg',
    '2026-02-07T15:24:53.365764',
    '2026-02-07T15:24:53.365764',
    1
);
INSERT INTO movie (ID, DESCRIPTION, LENGTH, NAME, YEAR, POSTER_FILEPATH, CREATED_AT, LAST_UPDATED, USER_ID) VALUES (
    6,
    'Die Leben zweier Auftragskiller, eines Boxers, einer Gangsterbraut und eines Pärchens, das Diner-Restaurants überfällt, werden in vier Episoden miteinander verwoben, die aus Gewalt und Erlösung bestehen.',
    214,
    'Pulp Fiction',
    1993,
    'movies/pulp_fiction_1993/pulp_fiction_1993.jpg',
    '2026-02-07T15:24:53.365764',
    '2026-02-07T15:24:53.365764',
    1
);
INSERT INTO movie (ID, DESCRIPTION, LENGTH, NAME, YEAR, POSTER_FILEPATH, CREATED_AT, LAST_UPDATED, USER_ID) VALUES (
    7,
    'Forrest Gump, obgleich nicht sonderlich mit Intelligenz gesegnet, war doch durch Zufall bei vielen historischen Augenblicken zugegen; Jenny Curran, seine große Liebe, entzieht sich ihm jedoch.',
    144,
    'Forrest Gump',
    1994,
    'movies/forrest_gump_1994/forrest_gump_1994.jpg',
    '2026-02-07T15:24:53.365764',
    '2026-02-07T15:24:53.365764',
    1
);
INSERT INTO movie (ID, DESCRIPTION, LENGTH, NAME, YEAR, POSTER_FILEPATH, CREATED_AT, LAST_UPDATED, USER_ID) VALUES (
    8,
    'Ein unter Schlaflosigkeit leidender Büroangestellter sucht nach einer Möglichkeit, sein Leben zu ändern, und trifft dabei auf einen sorglosen Seifenhändler, der im Untergrund einen Kampfclub unterhält, der sich als etwas noch viel Größeres herausstellt.',
    139,
    'Fight Club',
    1999,
    'movies/fight_club_1999/fight_club_1999.jpg',
    '2026-02-07T15:24:53.365764',
    '2026-02-07T15:24:53.365764',
    1
);
INSERT INTO movie (ID, DESCRIPTION, LENGTH, NAME, YEAR, POSTER_FILEPATH, CREATED_AT, LAST_UPDATED, USER_ID) VALUES (
    9,
    'Ein Computerhacker erfährt von mysteriösen Rebellen die Wahrheit über seine Realität und seine Rolle im Krieg gegen deren Kontrolleure.',
    136,
    'Matrix',
    1999,
    'movies/matrix_1999/matrix_1999.jpg',
    '2026-02-07T15:24:53.365764',
    '2026-02-07T15:24:53.365764',
    1
);
INSERT INTO movie (ID, DESCRIPTION, LENGTH, NAME, YEAR, POSTER_FILEPATH, CREATED_AT, LAST_UPDATED, USER_ID) VALUES (
    10,
    'Ein Forscherteam reist auf der Suche nach neuen Welten durch ein Wurmloch im Universum, um das Überleben der Menschheit sicherzustellen.',
    169,
    'Interstellar',
    2014,
    'movies/interstellar_2014/interstellar_2014.jpg',
    '2026-02-07T15:24:53.365764',
    '2026-02-07T15:24:53.365764',
    1
);
INSERT INTO movie (ID, DESCRIPTION, LENGTH, NAME, YEAR, POSTER_FILEPATH, CREATED_AT, LAST_UPDATED, USER_ID) VALUES (
    11,
    'Zwei Kripobeamte, ein Neuling und ein altgedienter Fuchs jagen einen Serienmörder, der die sieben Todsünden als seinen Modus Operandi verwendet.',
    127,
    'Sieben',
    1995,
    'movies/sieben_1995/sieben_1995.jpg',
    '2026-02-07T15:24:53.365764',
    '2026-02-07T15:24:53.365764',
    1
);

--
-- Initial values for table movie_genre_xref
--
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
	(SELECT ID FROM movie WHERE NAME = 'Inception' AND YEAR = 2012),
	(SELECT ID FROM movie_genre WHERE NAME = 'Action')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
	(SELECT ID FROM movie WHERE NAME = 'Inception' AND YEAR = 2012),
	(SELECT ID FROM movie_genre WHERE NAME = 'Abenteuer')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
	(SELECT ID FROM movie WHERE NAME = 'Inception' AND YEAR = 2012),
	(SELECT ID FROM movie_genre WHERE NAME = 'Sci-Fi')
);

INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Der Pate' AND YEAR = 1972),
	(SELECT ID FROM movie_genre WHERE NAME = 'Kriminalität')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Der Pate' AND YEAR = 1972),
	(SELECT ID FROM movie_genre WHERE NAME = 'Drama')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Der Pate' AND YEAR = 1972),
	(SELECT ID FROM movie_genre WHERE NAME = 'Gangster')
);

INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'The Dark Knight' AND YEAR = 2008),
	(SELECT ID FROM movie_genre WHERE NAME = 'Thriller')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'The Dark Knight' AND YEAR = 2008),
	(SELECT ID FROM movie_genre WHERE NAME = 'Kriminalität')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'The Dark Knight' AND YEAR = 2008),
	(SELECT ID FROM movie_genre WHERE NAME = 'Drama')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'The Dark Knight' AND YEAR = 2008),
	(SELECT ID FROM movie_genre WHERE NAME = 'Action')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'The Dark Knight' AND YEAR = 2008),
	(SELECT ID FROM movie_genre WHERE NAME = 'Superheld')
);

INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Der Herr der Ringe: Die Rückkehr des Königs' AND YEAR = 2003),
	(SELECT ID FROM movie_genre WHERE NAME = 'Drama')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Der Herr der Ringe: Die Rückkehr des Königs' AND YEAR = 2003),
	(SELECT ID FROM movie_genre WHERE NAME = 'Abenteuer')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Der Herr der Ringe: Die Rückkehr des Königs' AND YEAR = 2003),
	(SELECT ID FROM movie_genre WHERE NAME = 'Fantasy')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Der Herr der Ringe: Die Rückkehr des Königs' AND YEAR = 2003),
	(SELECT ID FROM movie_genre WHERE NAME = 'Action')
);

INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Schindlers Liste' AND YEAR = 1993),
	(SELECT ID FROM movie_genre WHERE NAME = 'Geschichte')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Schindlers Liste' AND YEAR = 1993),
	(SELECT ID FROM movie_genre WHERE NAME = 'Drama')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Schindlers Liste' AND YEAR = 1993),
	(SELECT ID FROM movie_genre WHERE NAME = 'Biographie')
);

INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Pulp Fiction' AND YEAR = 1993),
	(SELECT ID FROM movie_genre WHERE NAME = 'Kriminalität')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Pulp Fiction' AND YEAR = 1993),
	(SELECT ID FROM movie_genre WHERE NAME = 'Drama')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Pulp Fiction' AND YEAR = 1993),
	(SELECT ID FROM movie_genre WHERE NAME = 'Gangster')
);

INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Forrest Gump' AND YEAR = 1994),
	(SELECT ID FROM movie_genre WHERE NAME = 'Romanze')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Forrest Gump' AND YEAR = 1994),
	(SELECT ID FROM movie_genre WHERE NAME = 'Drama')
);

INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Fight Club' AND YEAR = 1999),
	(SELECT ID FROM movie_genre WHERE NAME = 'Drama')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Fight Club' AND YEAR = 1999),
	(SELECT ID FROM movie_genre WHERE NAME = 'Thriller')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Fight Club' AND YEAR = 1999),
	(SELECT ID FROM movie_genre WHERE NAME = 'Kriminalität')
);

INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Matrix' AND YEAR = 1999),
	(SELECT ID FROM movie_genre WHERE NAME = 'Sci-Fi')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Matrix' AND YEAR = 1999),
	(SELECT ID FROM movie_genre WHERE NAME = 'Action')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Matrix' AND YEAR = 1999),
	(SELECT ID FROM movie_genre WHERE NAME = 'Kampfkünste')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Matrix' AND YEAR = 1999),
	(SELECT ID FROM movie_genre WHERE NAME = 'Cyberpunk')
);

INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Interstellar' AND YEAR = 2014),
	(SELECT ID FROM movie_genre WHERE NAME = 'Sci-Fi')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Interstellar' AND YEAR = 2014),
	(SELECT ID FROM movie_genre WHERE NAME = 'Drama')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Interstellar' AND YEAR = 2014),
	(SELECT ID FROM movie_genre WHERE NAME = 'Abenteuer')
);

INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Sieben' AND YEAR = 1995),
	(SELECT ID FROM movie_genre WHERE NAME = 'Mysterium')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Sieben' AND YEAR = 1995),
	(SELECT ID FROM movie_genre WHERE NAME = 'Kriminalität')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Sieben' AND YEAR = 1995),
	(SELECT ID FROM movie_genre WHERE NAME = 'Drama')
);
INSERT INTO movie_genre_xref (MOVIE_ID, GENRE_ID) VALUES (
    (SELECT ID FROM movie WHERE NAME = 'Sieben' AND YEAR = 1995),
	(SELECT ID FROM movie_genre WHERE NAME = 'Thriller')
);

--
-- Initial values for table movie_rating
--
INSERT INTO movie_rating (MOVIE_ID, USER_ID, RATING) VALUES (
	(SELECT ID FROM movie WHERE NAME = 'Inception' AND YEAR = 2012),
	(SELECT ID FROM users WHERE USERNAME = 'admin'),
	3
);
INSERT INTO movie_rating (MOVIE_ID, USER_ID, RATING) VALUES (
	(SELECT ID FROM movie WHERE NAME = 'Inception' AND YEAR = 2012),
	(SELECT ID FROM users WHERE USERNAME = 'alfi'),
	5
);

--
-- Initial values for table series
--
INSERT INTO series (ID, DESCRIPTION, EPISODE_LENGTH, NAME, YEAR_START, YEAR_END, POSTER_FILEPATH, CREATED_AT, LAST_UPDATED, USER_ID) VALUES (
	1,
	'Ein Highschool-Chemielehrer, bei dem ein inoperabler Lungenkrebs diagnostiziert wurde, wendet sich der Herstellung und dem Verkauf von Methamphetamin zu, um die Zukunft seiner Familie zu sichern.',
	45,
	'Breaking Bad',
	2008,
	2013,
	'series/breaking_bad_2008/breaking_bad_2008.jpg',
	'2026-02-07T15:24:53.365764',
    '2026-02-07T15:24:53.365764',
    1
);

--
-- Initial values for table series_genre_xref
--
INSERT INTO series_genre_xref (SERIES_ID, GENRE_ID) VALUES (
	(SELECT ID FROM series WHERE NAME = 'Breaking Bad' AND YEAR_START = 2008),
	(SELECT ID FROM movie_genre WHERE NAME = 'Drama')
);
INSERT INTO series_genre_xref (SERIES_ID, GENRE_ID) VALUES (
	(SELECT ID FROM series WHERE NAME = 'Breaking Bad' AND YEAR_START = 2008),
	(SELECT ID FROM movie_genre WHERE NAME = 'Kriminalität')
);
INSERT INTO series_genre_xref (SERIES_ID, GENRE_ID) VALUES (
	(SELECT ID FROM series WHERE NAME = 'Breaking Bad' AND YEAR_START = 2008),
	(SELECT ID FROM movie_genre WHERE NAME = 'Thriller')
);

--
-- Initial values for table series_rating
--
INSERT INTO series_rating (SERIES_ID, USER_ID, RATING) VALUES (
	(SELECT ID FROM series WHERE NAME = 'Breaking Bad' AND YEAR_START = 2008),
	(SELECT ID FROM users WHERE USERNAME = 'admin'),
	3
);
INSERT INTO series_rating (SERIES_ID, USER_ID, RATING) VALUES (
	(SELECT ID FROM series WHERE NAME = 'Breaking Bad' AND YEAR_START = 2008),
	(SELECT ID FROM users WHERE USERNAME = 'alfi'),
	5
);

--
-- Initial values for table game
--
INSERT INTO game (ID, DESCRIPTION, NAME, YEAR, POSTER_FILEPATH, CREATED_AT, LAST_UPDATED, USER_ID) VALUES (
	1,
	'DAS NEUE FANTASY-ACTION-RPG. Erhebt Euch, Befleckter, und lasst Euch von der Gnade leiten, um die Macht des Eldenrings zu befehligen und zum Eldenfürsten im Zwischenland zu werden.',
	'Elden Ring',
	2022,
	'games/elden_ring_2022/elden_ring_2022.jpg',
	'2026-02-07T15:24:53.365764',
    '2026-02-07T15:24:53.365764',
    1
);
INSERT INTO game (ID, DESCRIPTION, NAME, YEAR, POSTER_FILEPATH, CREATED_AT, LAST_UPDATED, USER_ID) VALUES (
    2,
    'Mit Pokémon Smaragd wird die dritte Generation fortgesetzt. Das Abenteuer findet wie auch in Pokémon Rubin und Saphir in der Hoenn-Region statt, wo der Spieler die Machenschaften von Team Aqua und Team Magma verhindern muss. Die dritte Generation glänzt mit 135 neuen Pokémon; somit gibt es insgesamt 386. Wie bisher gibt es wieder drei Partner-Pokémon, von denen sich der Spieler eines aussuchen kann und anschließend in die Welt der Pokémon aufbricht, um einerseits der beste Pokémon-Trainer aller Zeiten zu werden und andererseits alle Pokémon zu fangen und den Pokédex zu vervollständigen.',
    'Pokémon Smaragd',
    2004,
    'games/pokémon_smaragd_2004/pokémon_smaragd_2004.jpg',
    '2026-08-06T15:24:53.365764',
    '2026-08-06T15:24:53.365764',
    1
);

--
-- Initial values for table game_genre_xref
--
INSERT INTO game_genre_xref (GAME_ID, GENRE_ID) VALUES (
	(SELECT ID FROM game WHERE NAME = 'Elden Ring' AND YEAR = 2022),
	(SELECT ID FROM game_genre WHERE NAME = 'Soulslike')
);
INSERT INTO game_genre_xref (GAME_ID, GENRE_ID) VALUES (
	(SELECT ID FROM game WHERE NAME = 'Elden Ring' AND YEAR = 2022),
	(SELECT ID FROM game_genre WHERE NAME = 'Action')
);
INSERT INTO game_genre_xref (GAME_ID, GENRE_ID) VALUES (
	(SELECT ID FROM game WHERE NAME = 'Elden Ring' AND YEAR = 2022),
	(SELECT ID FROM game_genre WHERE NAME = 'Fantasy')
);
INSERT INTO game_genre_xref (GAME_ID, GENRE_ID) VALUES (
	(SELECT ID FROM game WHERE NAME = 'Elden Ring' AND YEAR = 2022),
	(SELECT ID FROM game_genre WHERE NAME = 'Horror')
);

INSERT INTO game_genre_xref (GAME_ID, GENRE_ID) VALUES (
	(SELECT ID FROM game WHERE NAME = 'Pokémon Smaragd' AND YEAR = 2004),
	(SELECT ID FROM game_genre WHERE NAME = 'Rundenbasiert')
);
INSERT INTO game_genre_xref (GAME_ID, GENRE_ID) VALUES (
	(SELECT ID FROM game WHERE NAME = 'Pokémon Smaragd' AND YEAR = 2004),
	(SELECT ID FROM game_genre WHERE NAME = 'Rollenspiel')
);
INSERT INTO game_genre_xref (GAME_ID, GENRE_ID) VALUES (
	(SELECT ID FROM game WHERE NAME = 'Pokémon Smaragd' AND YEAR = 2004),
	(SELECT ID FROM game_genre WHERE NAME = 'Kreaturensammler')
);
INSERT INTO game_genre_xref (GAME_ID, GENRE_ID) VALUES (
	(SELECT ID FROM game WHERE NAME = 'Pokémon Smaragd' AND YEAR = 2004),
	(SELECT ID FROM game_genre WHERE NAME = 'Kampfspiel')
);

--
-- Initial values for table game_rating
--
INSERT INTO game_rating (GAME_ID, USER_ID, RATING) VALUES (
	(SELECT ID FROM game WHERE NAME = 'Elden Ring' AND YEAR = 2022),
	(SELECT ID FROM users WHERE USERNAME = 'admin'),
	4
);
INSERT INTO game_rating (GAME_ID, USER_ID, RATING) VALUES (
	(SELECT ID FROM game WHERE NAME = 'Elden Ring' AND YEAR = 2022),
	(SELECT ID FROM users WHERE USERNAME = 'alfi'),
	5
);

INSERT INTO game_rating (GAME_ID, USER_ID, RATING) VALUES (
	(SELECT ID FROM game WHERE NAME = 'Pokémon Smaragd' AND YEAR = 2004),
	(SELECT ID FROM users WHERE USERNAME = 'admin'),
	5
);

--
-- Initial values for table game_platform_xref
--
INSERT INTO game_platform_xref (GAME_ID, PLATFORM_ID) VALUES (
    (SELECT ID FROM game WHERE NAME = 'Elden Ring' AND YEAR = 2022),
    (SELECT ID FROM game_platform WHERE NAME = 'PC')
);

INSERT INTO game_platform_xref (GAME_ID, PLATFORM_ID) VALUES (
    (SELECT ID FROM game WHERE NAME = 'Pokémon Smaragd' AND YEAR = 2004),
    (SELECT ID FROM game_platform WHERE NAME = 'GBA')
);

--
-- Initial values for table music
--
INSERT INTO music (ID, NAME, ARTIST, DESCRIPTION, YEAR, POSTER_FILEPATH, CREATED_AT, LAST_UPDATED, USER_ID) VALUES (
    1,
    'Thriller',
    'Michael Jackson',
    '1. Wanna Be Startin'' Somethin'' 2. Baby Be Mine 3. The Girl Is Mine (ft. Paul McCartney) 4. Thriller 5. Beat It 6. Billie Jean 7. Human Nature 8. P.Y.T (Pretty Young Thing) 9. The Lady in My Life',
    1982,
    'music/thriller_michael_jackson_1982/thriller_michael_jackson_1982.jpg',
    '2026-08-12 09:19:16.288529',
    '2026-08-12 09:19:16.288529',
    1
);

--
-- Initial values for table music_genre_xref
--
INSERT INTO music_genre_xref (MUSIC_ID, GENRE_ID) VALUES (
	(SELECT ID FROM music WHERE NAME = 'Thriller' AND YEAR = 1982),
	(SELECT ID FROM music_genre WHERE NAME = 'Pop')
);
INSERT INTO music_genre_xref (MUSIC_ID, GENRE_ID) VALUES (
	(SELECT ID FROM music WHERE NAME = 'Thriller' AND YEAR = 1982),
	(SELECT ID FROM music_genre WHERE NAME = 'Rock')
);
INSERT INTO music_genre_xref (MUSIC_ID, GENRE_ID) VALUES (
	(SELECT ID FROM music WHERE NAME = 'Thriller' AND YEAR = 1982),
	(SELECT ID FROM music_genre WHERE NAME = 'R&B')
);

--
-- Initial values for table music_rating
--
INSERT INTO music_rating (MUSIC_ID, USER_ID, RATING) VALUES (
	(SELECT ID FROM music WHERE NAME = 'Thriller' AND YEAR = 1982),
	(SELECT ID FROM users WHERE USERNAME = 'admin'),
	4
);