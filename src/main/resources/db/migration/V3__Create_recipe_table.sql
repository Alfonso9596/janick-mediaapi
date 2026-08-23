--
-- Table structure for table recipe
--
CREATE TABLE recipe (
	ID bigint AUTO_INCREMENT,
	NAME VARCHAR(255) NOT NULL,
	DESCRIPTION TEXT,
	POSTER_FILEPATH VARCHAR(255),
	IS_VEGETARIAN boolean NOT NULL DEFAULT false,
	IS_VEGAN boolean NOT NULL DEFAULT false,
	IS_GLUTENFREE boolean NOT NULL DEFAULT false,
	IS_LACTOSEFREE boolean NOT NULL DEFAULT false,
	CREATED_AT datetime(6) NOT NULL,
	LAST_UPDATED datetime(6) NOT NULL,
	USER_ID bigint NOT NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (USER_ID) REFERENCES users (ID)
);

--
-- Table structure for table meal_type
--
CREATE TABLE meal_type (
	ID bigint AUTO_INCREMENT,
	NAME VARCHAR(255) NOT NULL,
	PRIMARY KEY (ID)
);

--
-- Table structure for table recipe_meal_type_xref
--
CREATE TABLE recipe_meal_type_xref (
	ID bigint AUTO_INCREMENT,
	RECIPE_ID bigint NOT NULL,
	MEAL_TYPE_ID bigint NOT NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (RECIPE_ID) REFERENCES recipe (ID),
	FOREIGN KEY (MEAL_TYPE_ID) REFERENCES meal_type (ID)
);

--
-- Table structure for table recipe_rating
--
CREATE TABLE recipe_rating (
	ID bigint AUTO_INCREMENT,
	RECIPE_ID bigint NOT NULL,
	USER_ID bigint NOT NULL,
	RATING INT NOT NULL,
	PRIMARY KEY (ID),
	UNIQUE KEY UK_RECIPE_RATING_USER (RECIPE_ID, USER_ID),
	FOREIGN KEY (RECIPE_ID) REFERENCES recipe (ID),
	FOREIGN KEY (USER_ID) REFERENCES users (ID)
);

--
-- Initial values for table meal_type
--
INSERT INTO meal_type (ID, NAME) VALUES (1, 'Drink');
INSERT INTO meal_type (ID, NAME) VALUES (2, 'Snack');
INSERT INTO meal_type (ID, NAME) VALUES (3, 'Suppe');
INSERT INTO meal_type (ID, NAME) VALUES (4, 'Sauce');
INSERT INTO meal_type (ID, NAME) VALUES (5, 'Salat');
INSERT INTO meal_type (ID, NAME) VALUES (6, 'Fleisch');
INSERT INTO meal_type (ID, NAME) VALUES (7, 'Fisch');
INSERT INTO meal_type (ID, NAME) VALUES (8, 'Eier');
INSERT INTO meal_type (ID, NAME) VALUES (9, 'Käse');
INSERT INTO meal_type (ID, NAME) VALUES (10, 'Gemüse');
INSERT INTO meal_type (ID, NAME) VALUES (11, 'Getreide');
INSERT INTO meal_type (ID, NAME) VALUES (12, 'Gebäck');
INSERT INTO meal_type (ID, NAME) VALUES (13, 'Dessert');