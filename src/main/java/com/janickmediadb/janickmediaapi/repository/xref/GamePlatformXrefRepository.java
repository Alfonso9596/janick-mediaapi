package com.janickmediadb.janickmediaapi.repository.xref;

import com.janickmediadb.janickmediaapi.entity.GameEntity;
import com.janickmediadb.janickmediaapi.entity.GamePlatformEntity;
import com.janickmediadb.janickmediaapi.entity.xref.GamePlatformXrefEntity;
import java.util.LinkedList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GamePlatformXrefRepository extends JpaRepository<GamePlatformXrefEntity, Integer> {

    @Query("SELECT gp.game FROM GamePlatformXrefEntity gp WHERE gp.platform.id = :platformId")
    LinkedList<GameEntity> findGamesByPlatform(int platformId);

    @Query("SELECT gp.platform FROM GamePlatformXrefEntity gp WHERE gp.game.id = :gameId")
    LinkedList<GamePlatformEntity> findPlatformsByGame(int gameId);

    @Query("SELECT gp FROM GamePlatformXrefEntity gp WHERE gp.game.id = :gameId")
    LinkedList<GamePlatformXrefEntity> findPlatformReferencesByGame(int gameId);
}
