package com.janickmediadb.janickmediaapi.service;

import com.janickmediadb.janickmediaapi.entity.GameEntity;
import com.janickmediadb.janickmediaapi.entity.security.UsersEntity;
import com.janickmediadb.janickmediaapi.entity.xref.GameRatingXrefEntity;
import com.janickmediadb.janickmediaapi.model.GameModel;
import com.janickmediadb.janickmediaapi.repository.xref.GameRatingXrefRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameRatingXrefService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameRatingXrefService.class);

    private final GameRatingXrefRepository gameRatingXrefRepository;

    @Autowired
    public GameRatingXrefService(GameRatingXrefRepository gameRatingXrefRepository) {
        this.gameRatingXrefRepository = gameRatingXrefRepository;
    }

    public void collectRatings(int gameId, GameModel model) {
        List<GameRatingXrefEntity> ratings = gameRatingXrefRepository.findAllByGameId(gameId);
        if (!ratings.isEmpty()) {
            model.setRatingAmount(ratings.size());
            double result = (double) (ratings.stream().mapToInt(GameRatingXrefEntity::getRating).sum()) / ratings.size();
            model.setRatingValue(result);
        } else {
            model.setRatingAmount(0);
            model.setRatingValue(0);
        }
    }

    public Optional<GameRatingXrefEntity> findByGameIdAndUser(int gameId, int userId) {
        return gameRatingXrefRepository.findByGameIdAndUserId(gameId, userId);
    }

    public void deleteGameRatingReferenceByGameId(int gameId) {
        List<GameRatingXrefEntity> ratings = gameRatingXrefRepository.findAllByGameId(gameId);
        if (!ratings.isEmpty()) {
            gameRatingXrefRepository.deleteAll(ratings);
        }
    }

    public GameRatingXrefEntity addRating(GameEntity game, UsersEntity user, int rating) {
        GameRatingXrefEntity gameRatingXrefEntity = new GameRatingXrefEntity();
        gameRatingXrefEntity.setGame(game);
        gameRatingXrefEntity.setUser(user);
        gameRatingXrefEntity.setRating(rating);

        LOGGER.info("addRating: Saving game rating {} for {}", rating, game.getName());
        return gameRatingXrefRepository.save(gameRatingXrefEntity);
    }

    public GameRatingXrefEntity updateRating(GameRatingXrefEntity gameRating, int rating) {
        gameRating.setRating(rating);
        return gameRatingXrefRepository.save(gameRating);
    }
}
