package com.janickmediadb.janickmediaapi.repository.xref;

import com.janickmediadb.janickmediaapi.entity.xref.GameRatingXrefEntity;
import java.util.LinkedList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRatingXrefRepository extends JpaRepository<GameRatingXrefEntity, Integer> {

    LinkedList<GameRatingXrefEntity> findAllByGameId(int gameId);

    Optional<GameRatingXrefEntity> findByGameIdAndUserId(int gameId, int userId);
}
