package com.janick_mediadb.janick_mediaapi.repository.xref;

import com.janick_mediadb.janick_mediaapi.entity.GameEntity;
import com.janick_mediadb.janick_mediaapi.entity.GamePlatformEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.GamePlatformXrefEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.LinkedList;

public interface GamePlatformXrefRepository extends JpaRepository<GamePlatformXrefEntity, Integer> {

    @Query("SELECT gp.game FROM GamePlatformXrefEntity gp WHERE gp.platform.id = :platformId")
    LinkedList<GameEntity> findGamesByPlatform(int platformId);

    @Query("SELECT gp.platform FROM GamePlatformXrefEntity gp WHERE gp.game.id = :gameId")
    LinkedList<GamePlatformEntity> findPlatformsByGame(int gameId);

    @Query("SELECT gp FROM GamePlatformXrefEntity gp WHERE gp.game.id = :gameId")
    LinkedList<GamePlatformXrefEntity> findPlatformReferencesByGame(int gameId);
}
