package com.janick_mediadb.janick_mediaapi.service;

import com.janick_mediadb.janick_mediaapi.entity.GameEntity;
import com.janick_mediadb.janick_mediaapi.entity.GameGenreEntity;
import com.janick_mediadb.janick_mediaapi.exception.BadRequestException;
import com.janick_mediadb.janick_mediaapi.exception.NotFoundException;
import com.janick_mediadb.janick_mediaapi.input.MovieGenreInput;
import com.janick_mediadb.janick_mediaapi.model.MovieGenreModel;
import com.janick_mediadb.janick_mediaapi.repository.GameGenreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GameGenreService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameGenreService.class);

    private static final String GENRE_NAME_NOT_EXIST = "Genre with name {0} does not exist";
    private static final String GENRE_ID_NOT_EXIST = "Genre with id {0} does not exist";
    private static final String GENRE_ALREADY_REGISTERED = "The genre {0} is already registered";

    private final GameGenreRepository gameGenreRepository;

    private final GameGenreXrefService gameGenreXrefService;

    @Autowired
    public GameGenreService(GameGenreRepository gameGenreRepository, GameGenreXrefService gameGenreXrefService) {
        this.gameGenreRepository = gameGenreRepository;
        this.gameGenreXrefService = gameGenreXrefService;
    }

    public List<MovieGenreModel> getAllGenres() {
        List<MovieGenreModel> genres = new ArrayList<>();
        gameGenreRepository.findAll().forEach(genre -> genres.add(genre.toModel()));

        LOGGER.info("getAllGenres: Found a total of {} genres", genres.size());
        return genres;
    }

    public GameGenreEntity getGenreByName(String name) {
        Optional<GameGenreEntity> opGenre = gameGenreRepository.findByName(name);
        if (opGenre.isPresent()) {
            LOGGER.info("getGenreByName: Found genre with name {}", name);
            return opGenre.get();
        } else {
            String message = MessageFormat.format(GENRE_NAME_NOT_EXIST, name);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
    }

    public MovieGenreModel getGenreById(int id) {
        Optional<GameGenreEntity> opGenre = gameGenreRepository.findById(id);
        if (opGenre.isPresent()) {
            LOGGER.info("getGenreById: Found genre with id {}", id);
            return opGenre.get().toModel();
        } else {
            String message = MessageFormat.format(GENRE_ID_NOT_EXIST, id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
    }

    public MovieGenreModel saveGenre(MovieGenreInput movieGenreInput) {
        List<GameGenreEntity> genres = getAllGenreEntities();
        Optional<GameGenreEntity> op = genres.stream()
                .filter(genreEntity -> movieGenreInput.getName().equalsIgnoreCase(genreEntity.getName()))
                .findAny();

        if (op.isPresent()) {
            String message = MessageFormat.format(GENRE_ALREADY_REGISTERED, movieGenreInput.getName());
            LOGGER.error(message);
            throw new BadRequestException(message);
        }

        GameGenreEntity gameGenreEntity = new GameGenreEntity();
        gameGenreEntity.fromInput(movieGenreInput);

        LOGGER.info("saveGenre: Saving genre {}", gameGenreEntity.toModel());

        return gameGenreRepository.save(gameGenreEntity).toModel();
    }

    public String deleteGenre(int id) {
        Optional<GameGenreEntity> opGenre = gameGenreRepository.findById(id);
        if (opGenre.isEmpty()) {
            String message = MessageFormat.format(GENRE_ID_NOT_EXIST, id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }

        List<GameEntity> gameEntities = gameGenreXrefService.findGamesByGenre(id);
        if (!gameEntities.isEmpty()) {
            String message = MessageFormat.format("Genre {0} cannot be deleted, because there are still games with this genre", opGenre.get().getName());
            LOGGER.error(message);
            throw new BadRequestException(message);
        }

        GameGenreEntity gameGenreEntity = opGenre.get();
        LOGGER.info("deleteGenre: Deleting genre {}", gameGenreEntity.toModel());
        gameGenreRepository.delete(gameGenreEntity);
        return MessageFormat.format("The genre {0} has been deleted", gameGenreEntity.getName());
    }

    private List<GameGenreEntity> getAllGenreEntities() {
        return new ArrayList<>(gameGenreRepository.findAll());
    }
}
