package com.janick_mediadb.janick_mediaapi.service;

import com.janick_mediadb.janick_mediaapi.auth.UserDetailsImpl;
import com.janick_mediadb.janick_mediaapi.entity.GameEntity;
import com.janick_mediadb.janick_mediaapi.entity.GameGenreEntity;
import com.janick_mediadb.janick_mediaapi.entity.GamePlatformEntity;
import com.janick_mediadb.janick_mediaapi.entity.security.UsersEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.GameRatingXrefEntity;
import com.janick_mediadb.janick_mediaapi.exception.BadRequestException;
import com.janick_mediadb.janick_mediaapi.exception.InternalServerException;
import com.janick_mediadb.janick_mediaapi.exception.NotFoundException;
import com.janick_mediadb.janick_mediaapi.exception.UnauthorizedException;
import com.janick_mediadb.janick_mediaapi.input.GameInput;
import com.janick_mediadb.janick_mediaapi.input.GamePlatformInput;
import com.janick_mediadb.janick_mediaapi.input.GenreInput;
import com.janick_mediadb.janick_mediaapi.model.FileInfoModel;
import com.janick_mediadb.janick_mediaapi.model.GameModel;
import com.janick_mediadb.janick_mediaapi.model.RatingUpdateModel;
import com.janick_mediadb.janick_mediaapi.model.response.GameResponse;
import com.janick_mediadb.janick_mediaapi.model.response.GameSearchCriteria;
import com.janick_mediadb.janick_mediaapi.model.specifications.GameSpecification;
import com.janick_mediadb.janick_mediaapi.repository.GameRepository;
import com.janick_mediadb.janick_mediaapi.utils.FileUtility;
import com.janick_mediadb.janick_mediaapi.utils.NamingUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.*;

@Service
public class GameService implements MediaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameService.class);
    private static final String GAME_FILES_PATH = "games/";
    public static final String GAME_WITH_ID_DOES_NOT_EXIST = "Game with id {0} does not exist";

    private final GameRepository gameRepository;

    private final GameGenreXrefService gameGenreXrefService;

    private final GameRatingXrefService gameRatingXrefService;

    private final GamePlatformXrefService gamePlatformXrefService;

    private final GameGenreService musicGenreService;

    private final GamePlatformService gamePlatformService;

    private final UserService userService;

    @Autowired
    public GameService(GameRepository gameRepository, GameGenreXrefService gameGenreXrefService, GameRatingXrefService gameRatingXrefService, GamePlatformXrefService gamePlatformXrefService, GameGenreService gameGenreService, GamePlatformService gamePlatformService, UserService userService) {
        this.gameRepository = gameRepository;
        this.gameGenreXrefService = gameGenreXrefService;
        this.gameRatingXrefService = gameRatingXrefService;
        this.gamePlatformXrefService = gamePlatformXrefService;
        this.musicGenreService = gameGenreService;
        this.gamePlatformService = gamePlatformService;
        this.userService = userService;
    }

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
            gamePlatformXrefService.collectPlatforms(g.getId(), model);
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
            gamePlatformXrefService.collectPlatforms(model.getId(), model);
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
                .filter(game -> gameInput.getName().equals(game.getName()))
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
                GenreInput input = new GenreInput();
                input.setName(genre);
                try {
                    musicGenreService.saveGenre(input);
                } catch (BadRequestException _) {
                    LOGGER.warn("Genre {} already exists", genre);
                }
            }

            for (String genre : gameInput.getGenres()) {
                GameGenreEntity gameGenreEntity = musicGenreService.getGenreByName(genre);
                genreEntities.add(gameGenreEntity);
            }
        }

        List<GamePlatformEntity>  platformEntities = new ArrayList<>();
        if (!gameInput.getPlatforms().isEmpty()) {
            for (String platform : gameInput.getPlatforms()) {
                GamePlatformInput input = new GamePlatformInput();
                input.setName(platform);
                try {
                    gamePlatformService.savePlatform(input);
                } catch (BadRequestException _) {
                    LOGGER.warn("Platform {} already exists", platform);
                }
            }

            for (String platform : gameInput.getPlatforms()) {
                GamePlatformEntity platformEntity = gamePlatformService.getPlatformByName(platform);
                platformEntities.add(platformEntity);
            }
        }

        GameEntity gameEntity = new GameEntity();
        gameEntity.fromInput(gameInput);

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UsersEntity user = userService.getUserByUsername(userDetails.getUsername());
        gameEntity.setUser(user);

        String filename = NamingUtility.renameTitleForFilepath(gameInput.getName(), gameInput.getYear());

        String posterFilename = GAME_FILES_PATH + filename + "/" + filename + FileStorageServiceImpl.POSTER_FILE_TYPE;
        gameEntity.setPosterFilepath(posterFilename);

        Instant currentTime = Instant.now();

        gameEntity.setCreatedAt(currentTime);
        gameEntity.setLastUpdated(currentTime);

        gameEntity = gameRepository.save(gameEntity);
        LOGGER.info("saveGame: Saving game {}", gameEntity.toModel());
        gameGenreXrefService.saveGameGenreXref(gameEntity, genreEntities);
        gamePlatformXrefService.saveGamePlatformXref(gameEntity, platformEntities);

        return gameEntity.toModel();
    }

    public GameModel updateGame(int id, GameInput gameInput) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UsersEntity user = userService.getUserByUsername(userDetails.getUsername());

        GameEntity gameEntity = gameRepository.findById(id).orElseThrow(() -> new NotFoundException("Game with id " + id + " not found!"));

        if (gameEntity.getUser().getId() != user.getId() && user.getRoles().stream().noneMatch(role -> role.getName().equals("ADMIN"))) {
            throw new UnauthorizedException("User " + user.getUsername() + " is not allowed to update game with id " + id);
        }

        List<GameGenreEntity> genreEntities = new ArrayList<>();
        if (!gameInput.getGenres().isEmpty()) {
            for (String genre : gameInput.getGenres()) {
                GenreInput input = new GenreInput();
                input.setName(genre);
                try {
                    musicGenreService.saveGenre(input);
                } catch (BadRequestException _) {
                    LOGGER.warn("Genre {} already exists", genre);
                }
            }

            for (String genre : gameInput.getGenres()) {
                GameGenreEntity gameGenreEntity = musicGenreService.getGenreByName(genre);
                genreEntities.add(gameGenreEntity);
            }
        }

        List<GamePlatformEntity> platformEntities = new ArrayList<>();
        if (!gameInput.getPlatforms().isEmpty()) {
            for (String platform : gameInput.getPlatforms()) {
                GamePlatformInput input = new GamePlatformInput();
                input.setName(platform);
                try {
                    gamePlatformService.savePlatform(input);
                } catch (BadRequestException _) {
                    LOGGER.warn("Platform {} already exists", platform);
                }
            }

            for (String platform : gameInput.getPlatforms()) {
                GamePlatformEntity platformEntity = gamePlatformService.getPlatformByName(platform);
                platformEntities.add(platformEntity);
            }
        }

        gameGenreXrefService.deleteGameGenreReferenceByGameId(id);
        gamePlatformXrefService.deleteGamePlatformReferenceByGameId(id);
        mapToEntity(gameEntity, gameInput);

        Instant currentTime = Instant.now();
        gameEntity.setLastUpdated(currentTime);
        gameEntity = gameRepository.save(gameEntity);
        LOGGER.info("updateGame: Updating game {}", gameEntity.toModel());
        gameGenreXrefService.saveGameGenreXref(gameEntity, genreEntities);
        gamePlatformXrefService.saveGamePlatformXref(gameEntity, platformEntities);

        return gameEntity.toModel();
    }

    public ResponseEntity<String> deleteGame(int id) {
        Optional<GameEntity> opGame = gameRepository.findById(id);
        if (opGame.isEmpty()) {
            String message = MessageFormat.format(GAME_WITH_ID_DOES_NOT_EXIST, id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
        GameEntity gameEntity = opGame.get();
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal.getId() != gameEntity.getUser().getId() && principal.getAuthorities().stream().noneMatch(role -> Objects.equals(role.getAuthority(), "ADMIN"))) {
            throw new UnauthorizedException("User " + principal.getUsername() + " is not allowed to delete game " + gameEntity.getName());
        }

        gameGenreXrefService.deleteGameGenreReferenceByGameId(id);
        gameRatingXrefService.deleteGameRatingReferenceByGameId(id);
        gamePlatformXrefService.deleteGamePlatformReferenceByGameId(id);

        LOGGER.info("deleteGame: Deleting game {}", gameEntity.toModel());
        gameRepository.delete(gameEntity);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(MessageFormat.format("The game {0} has been deleted", gameEntity.getName()));
    }

    public String rateGame(RatingUpdateModel ratingUpdateModel) {
        Optional<GameEntity> opGame = gameRepository.findById(ratingUpdateModel.getId());
        if (opGame.isEmpty()) {
            String message = MessageFormat.format(GAME_WITH_ID_DOES_NOT_EXIST, ratingUpdateModel.getId());
            LOGGER.error(message);
            throw new NotFoundException(message);
        }

        GameEntity gameEntity = opGame.get();
        UserDetailsImpl userDeatils = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<GameRatingXrefEntity> existingRating = gameRatingXrefService.findByGameIdAndUser(gameEntity.getId(), userDeatils.getId());
        UsersEntity user = userService.getUserByUsername(userDeatils.getUsername());

        if (existingRating.isEmpty()) {
            gameRatingXrefService.addRating(gameEntity, user, ratingUpdateModel.getRating());
        } else {
            gameRatingXrefService.updateRating(existingRating.get(), ratingUpdateModel.getRating());
        }

        return MessageFormat.format("{0} has been rated with {1}", gameEntity.getName(), ratingUpdateModel.getRating());
    }

    public ResponseEntity<List<FileInfoModel>> getGameFiles(int id) {
        Optional<GameEntity> opGame = gameRepository.findById(id);
        if (opGame.isEmpty()) {
            String message = MessageFormat.format(GAME_WITH_ID_DOES_NOT_EXIST, id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
        GameEntity gameEntity = opGame.get();
        String fileStorageName = NamingUtility.renameTitleForFilepath(gameEntity.getName(), gameEntity.getYear());
        Path filePath = FileStorageServiceImpl.games.resolve(fileStorageName).resolve("files");

        try {
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath);
            }
        } catch (IOException e) {
            throw new InternalServerException("Could not create directory " + filePath, e);
        }
        List<FileInfoModel> fileInfoModels = FileUtility.getDirList(filePath.toFile());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfoModels);
    }

    @Override
    public Path getMediaFilePath(int mediaId) {
        Optional<GameEntity> opGame = gameRepository.findById(mediaId);
        if (opGame.isEmpty()) {
            String message = MessageFormat.format(GAME_WITH_ID_DOES_NOT_EXIST, mediaId);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }

        GameEntity gameEntity = opGame.get();
        String fileStorageName = NamingUtility.renameTitleForFilepath(gameEntity.getName(), gameEntity.getYear());
        return FileStorageServiceImpl.games.resolve(fileStorageName).resolve("files");
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

    private void mapToEntity(GameEntity existingGame, GameInput gameInput) {
        if (gameInput.getName() != null && !gameInput.getName().isEmpty()) {
            existingGame.setName(gameInput.getName());
        }
        if (gameInput.getYear() != null && !gameInput.getYear().isEmpty()) {
            existingGame.setYear(gameInput.getYear());
        }
        if (gameInput.getDescription() != null && !gameInput.getDescription().isEmpty()) {
            existingGame.setDescription(gameInput.getDescription());
        }
    }
}
