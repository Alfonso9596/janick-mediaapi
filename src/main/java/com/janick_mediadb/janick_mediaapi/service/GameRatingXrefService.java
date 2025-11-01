package com.janick_mediadb.janick_mediaapi.service;

import com.janick_mediadb.janick_mediaapi.entity.GameEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.GameRatingXrefEntity;
import com.janick_mediadb.janick_mediaapi.model.GameModel;
import com.janick_mediadb.janick_mediaapi.repository.xref.GameRatingXrefRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameRatingXrefService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameRatingXrefService.class);

    @Autowired
    private GameRatingXrefRepository gameRatingXrefRepository;

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

    public void deleteGameRatingReferenceByGameId(int gameId) {
        List<GameRatingXrefEntity> ratings = gameRatingXrefRepository.findAllByGameId(gameId);
        if (!ratings.isEmpty()) {
            gameRatingXrefRepository.deleteAll(ratings);
        }
    }

    public void addRating(GameEntity game, int rating) {
        GameRatingXrefEntity gameRatingXrefEntity = new GameRatingXrefEntity();
        gameRatingXrefEntity.setGame(game);
        gameRatingXrefEntity.setRating(rating);

        LOGGER.info("addRating: Saving game rating {} for {}", rating, game.getName());
        gameRatingXrefRepository.save(gameRatingXrefEntity);
    }
}
