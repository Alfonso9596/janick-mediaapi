package com.janick_mediadb.janick_mediaapi.service;

import com.janick_mediadb.janick_mediaapi.entity.GameEntity;
import com.janick_mediadb.janick_mediaapi.entity.GameGenreEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.GameGenreXrefEntity;
import com.janick_mediadb.janick_mediaapi.model.GameModel;
import com.janick_mediadb.janick_mediaapi.repository.xref.GameGenreXrefRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameGenreXrefService {

    @Autowired
    private GameGenreXrefRepository gameGenreXrefRepository;

    public List<GameEntity> findGamesByGenre(int genreId) {
        return gameGenreXrefRepository.findGamesByGenre(genreId);
    }

    public void deleteGameGenreReferenceByGameId(int gameId) {
        List<GameGenreXrefEntity> ggs = gameGenreXrefRepository.findGameGenreReferencesByGame(gameId);
        if (!ggs.isEmpty()) {
            gameGenreXrefRepository.deleteAll(ggs);
        }
    }

    public void saveGameGenreXref(GameEntity game, List<GameGenreEntity> genres) {
        if (!genres.isEmpty()) {
            for (GameGenreEntity genre : genres) {
                GameGenreXrefEntity gg = new GameGenreXrefEntity();
                gg.setGame(game);
                gg.setGenre(genre);
                gameGenreXrefRepository.save(gg);
            }
        }
    }

    public void collectGenres(int gameId, GameModel gameModel) {
        List<GameGenreEntity> genres = gameGenreXrefRepository.findGenresByGame(gameId);
        gameModel.setGenres(GameGenreEntity.toModels(genres));
    }
}
