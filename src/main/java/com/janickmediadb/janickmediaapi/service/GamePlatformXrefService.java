package com.janickmediadb.janickmediaapi.service;

import com.janickmediadb.janickmediaapi.entity.GameEntity;
import com.janickmediadb.janickmediaapi.entity.GamePlatformEntity;
import com.janickmediadb.janickmediaapi.entity.xref.GamePlatformXrefEntity;
import com.janickmediadb.janickmediaapi.model.GameModel;
import com.janickmediadb.janickmediaapi.repository.xref.GamePlatformXrefRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GamePlatformXrefService {

    private final GamePlatformXrefRepository gamePlatformXrefRepository;

    @Autowired
    public GamePlatformXrefService(GamePlatformXrefRepository gamePlatformXrefRepository) {
        this.gamePlatformXrefRepository = gamePlatformXrefRepository;
    }

    public List<GameEntity> findGamesByPlatform(int platformId) {
        return gamePlatformXrefRepository.findGamesByPlatform(platformId);
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
