package com.janick_mediadb.janick_mediaapi.repository.xref;

import com.janick_mediadb.janick_mediaapi.entity.GameEntity;
import com.janick_mediadb.janick_mediaapi.entity.GameGenreEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.GameGenreXrefEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.LinkedList;

public interface GameGenreXrefRepository extends JpaRepository<GameGenreXrefEntity, Integer> {

    @Query("SELECT gg.game FROM GameGenreXrefEntity gg WHERE gg.genre.id = :genreId")
    LinkedList<GameEntity> findGamesByGenre(int genreId);

    @Query("SELECT gg.genre FROM GameGenreXrefEntity gg WHERE gg.game.id = :gameId")
    LinkedList<GameGenreEntity> findGenresByGame(int gameId);

    @Query("SELECT gg FROM GameGenreXrefEntity gg WHERE gg.game.id = :gameId")
    LinkedList<GameGenreXrefEntity> findGameGenreReferencesByGame(int gameId);
}
