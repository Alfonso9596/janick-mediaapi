package com.janick_mediadb.janick_mediaapi.service;

import com.janick_mediadb.janick_mediaapi.entity.GameEntity;
import com.janick_mediadb.janick_mediaapi.entity.GamePlatformEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.GamePlatformXrefEntity;
import com.janick_mediadb.janick_mediaapi.model.GameModel;
import com.janick_mediadb.janick_mediaapi.repository.xref.GamePlatformXrefRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GamePlatformXrefService {

    private final GamePlatformXrefRepository gamePlatformXrefRepository;

    @Autowired
    public GamePlatformXrefService(GamePlatformXrefRepository gamePlatformXrefRepository) {
        this.gamePlatformXrefRepository = gamePlatformXrefRepository;
    }

    public List<GameEntity> findGamesByPlatform(int platformId) {
        return  gamePlatformXrefRepository.findGamesByPlatform(platformId);
    }

    public void deleteGamePlatformReferenceByGameId(int gameId) {
        List<GamePlatformXrefEntity> gps = gamePlatformXrefRepository.findPlatformReferencesByGame(gameId);
        if (!gps.isEmpty()) {
            gamePlatformXrefRepository.deleteAll(gps);
        }
    }

    public void saveGamePlatformXref(GameEntity game, List<GamePlatformEntity> platforms) {
        if (!platforms.isEmpty()) {
            for (GamePlatformEntity platform : platforms) {
                GamePlatformXrefEntity gp = new GamePlatformXrefEntity();
                gp.setGame(game);
                gp.setPlatform(platform);
                gamePlatformXrefRepository.save(gp);
            }
        }
    }

    public void collectPlatforms(int gameId, GameModel gameModel) {
        List<GamePlatformEntity> platforms = gamePlatformXrefRepository.findPlatformsByGame(gameId);
        gameModel.setPlatforms(GamePlatformEntity.toModels(platforms));
    }
}
