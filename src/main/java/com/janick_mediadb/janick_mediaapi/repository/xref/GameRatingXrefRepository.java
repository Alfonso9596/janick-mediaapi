package com.janick_mediadb.janick_mediaapi.repository.xref;

import com.janick_mediadb.janick_mediaapi.entity.xref.GameRatingXrefEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.LinkedList;

public interface GameRatingXrefRepository extends JpaRepository<GameRatingXrefEntity, Integer> {

    LinkedList<GameRatingXrefEntity> findAllByGameId(int gameId);
}
