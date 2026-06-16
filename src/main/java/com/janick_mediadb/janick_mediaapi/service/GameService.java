package com.janick_mediadb.janick_mediaapi.service;

import com.janick_mediadb.janick_mediaapi.auth.UserDetailsImpl;
import com.janick_mediadb.janick_mediaapi.entity.GameEntity;
import com.janick_mediadb.janick_mediaapi.entity.GameGenreEntity;
import com.janick_mediadb.janick_mediaapi.entity.security.UsersEntity;
import com.janick_mediadb.janick_mediaapi.exception.BadRequestException;
import com.janick_mediadb.janick_mediaapi.exception.NotFoundException;
import com.janick_mediadb.janick_mediaapi.input.GameInput;
import com.janick_mediadb.janick_mediaapi.input.MovieGenreInput;
import com.janick_mediadb.janick_mediaapi.model.GameModel;
import com.janick_mediadb.janick_mediaapi.model.response.GameResponse;
import com.janick_mediadb.janick_mediaapi.model.response.GameSearchCriteria;
import com.janick_mediadb.janick_mediaapi.model.specifications.GameSpecification;
import com.janick_mediadb.janick_mediaapi.repository.GameRepository;
import com.janick_mediadb.janick_mediaapi.utils.NamingUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameService.class);
    private static final String GAME_FILES_PATH = "games/";
    public static final String GAME_WITH_ID_DOES_NOT_EXIST = "Game with id {0} does not exist";

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameGenreXrefService gameGenreXrefService;

    @Autowired
    private GameRatingXrefService gameRatingXrefService;

    @Autowired
    private GameGenreService genreService;

    @Autowired
    private UserService userService;

    public GameResponse getAllGames(int page, int pageSize, String sortBy, String sortDir, GameSearchCriteria criteria) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Specification<GameEntity> specification = createSpecs(criteria);

        Page<GameEntity> games = gameRepository.findAll(specification, pageable);

        List<GameEntity> listOfGames = games.getContent();
        List<GameModel> content = listOfGames.stream().map(g -> {
            GameModel model = g.toModel();
            gameGenreXrefService.collectGenres(g.getId(), model);
            gameRatingXrefService.collectRatings(g.getId(), model);
            return model;
        }).toList();

        GameResponse gameResponse = new GameResponse();
        gameResponse.setContent(content);
        gameResponse.setPage(games.getNumber());
        gameResponse.setPageSize(games.getSize());
        gameResponse.setTotalElements(games.getTotalElements());
        gameResponse.setTotalPages(games.getTotalPages());
        gameResponse.setLast(games.isLast());

        return gameResponse;
    }

    public List<String> getAllGameNames() {
        List<String> gameNames = gameRepository.getAllGameNames();
        gameNames.sort(Comparator.naturalOrder());
        return gameNames;
    }

    public GameModel getGameById(int id) {
        Optional<GameEntity> opGame = gameRepository.findById(id);
        if (opGame.isPresent()) {
            LOGGER.info("getGameById: Found game with id {}", id);
            GameModel model = opGame.get().toModel();
            gameGenreXrefService.collectGenres(model.getId(), model);
            gameRatingXrefService.collectRatings(model.getId(), model);
            return model;
        } else {
            String message = MessageFormat.format(GAME_WITH_ID_DOES_NOT_EXIST, id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
    }

    public GameModel saveGame(GameInput gameInput) {
        List<GameEntity> gameEntities = getAllGameEntities();
        Optional<GameEntity> opGame = gameEntities.stream()
                .filter(game -> gameInput.getName().equals(game.getYear()))
                .filter(game -> gameInput.getYear().equals(game.getYear()))
                .findAny();

        if (opGame.isPresent()) {
            String message = MessageFormat.format("The game {0} is already registered", gameInput.getName());
            LOGGER.error(message);
            throw new BadRequestException(message);
        }

        List<GameGenreEntity> genreEntities = new ArrayList<>();
        if (!gameInput.getGenres().isEmpty()) {
            for (String genre : gameInput.getGenres()) {
                MovieGenreInput input = new MovieGenreInput();
                input.setName(genre);
                try {
                    genreService.saveGenre(input);
                } catch (BadRequestException e) {
                    LOGGER.warn("Genre {} already exists", genre);
                }
            }

            for (String genre : gameInput.getGenres()) {
                GameGenreEntity gameGenreEntity = genreService.getGenreByName(genre);
                genreEntities.add(gameGenreEntity);
            }
        }

        GameEntity gameEntity = new GameEntity();
        gameEntity.fromInput(gameInput);

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UsersEntity user = userService.getUserByUsername(userDetails.getUsername());
        gameEntity.setUser(user);

        String filename = NamingUtility.renameTitleForFilepath(gameInput.getName()) + "_" + gameInput.getYear();

        String posterFilename = GAME_FILES_PATH + filename + "/" + filename + FileStorageServiceImpl.POSTER_FILE_TYPE;
        gameEntity.setPosterFilepath(posterFilename);

        Instant currentTime = Instant.now();

        gameEntity.setCreatedAt(currentTime);
        gameEntity.setLastUpdated(currentTime);

        gameEntity = gameRepository.save(gameEntity);
        LOGGER.info("saveGame: Saving game {}", gameEntity.toModel());
        gameGenreXrefService.saveGameGenreXref(gameEntity, genreEntities);

        return gameEntity.toModel();
    }

    public String deleteGame(int id) {
        Optional<GameEntity> opGame = gameRepository.findById(id);
        if (opGame.isEmpty()) {
            String message = MessageFormat.format(GAME_WITH_ID_DOES_NOT_EXIST, id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }

        gameGenreXrefService.deleteGameGenreReferenceByGameId(id);
        gameRatingXrefService.deleteGameRatingReferenceByGameId(id);

        GameEntity gameEntity = opGame.get();
        LOGGER.info("deleteGame: Deleting game {}", gameEntity.toModel());
        gameRepository.delete(gameEntity);
        return MessageFormat.format("The game {0} has been deleted", gameEntity.getName());
    }

    public String rateGame(int id, int rating) {
        Optional<GameEntity> opGame = gameRepository.findById(id);
        if (opGame.isEmpty()) {
            String message = MessageFormat.format(GAME_WITH_ID_DOES_NOT_EXIST, id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
        GameEntity gameEntity = opGame.get();
        gameRatingXrefService.addRating(gameEntity, rating);
        return MessageFormat.format("{0} has been rated with {1}", gameEntity.getName(), rating);
    }

    private List<GameEntity> getAllGameEntities() {
        return new ArrayList<>(gameRepository.findAll());
    }

    private Specification<GameEntity> createSpecs(GameSearchCriteria criteria) {
        Specification<GameEntity> spec = Specification.unrestricted();

        if (criteria.getName() != null) {
            spec = spec.and(GameSpecification.likeName(criteria.getName()));
        }

        if (criteria.getYear() > 0) {
            spec = spec.and(GameSpecification.equalsYear(criteria.getYear()));
        }

        if (criteria.getGenre() != null) {
            spec = spec.and(GameSpecification.containsGenre(criteria.getGenre()));
        }

        if (criteria.getPlatform() != null) {
            spec = spec.and(GameSpecification.containsPlatform(criteria.getPlatform()));
        }

        return spec;
    }
}
