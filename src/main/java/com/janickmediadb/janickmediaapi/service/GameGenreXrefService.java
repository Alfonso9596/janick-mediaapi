package com.janickmediadb.janickmediaapi.service;

import com.janickmediadb.janickmediaapi.entity.GameEntity;
import com.janickmediadb.janickmediaapi.entity.GameGenreEntity;
import com.janickmediadb.janickmediaapi.entity.xref.GameGenreXrefEntity;
import com.janickmediadb.janickmediaapi.model.GameModel;
import com.janickmediadb.janickmediaapi.repository.xref.GameGenreXrefRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameGenreXrefService {

    private final GameGenreXrefRepository gameGenreXrefRepository;

    @Autowired
    public GameGenreXrefService(GameGenreXrefRepository gameGenreXrefRepository) {
        this.gameGenreXrefRepository = gameGenreXrefRepository;
    }

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
