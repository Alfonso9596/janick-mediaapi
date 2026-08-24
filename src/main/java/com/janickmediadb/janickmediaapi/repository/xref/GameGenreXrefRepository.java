package com.janickmediadb.janickmediaapi.repository.xref;

import com.janickmediadb.janickmediaapi.entity.GameEntity;
import com.janickmediadb.janickmediaapi.entity.GameGenreEntity;
import com.janickmediadb.janickmediaapi.entity.xref.GameGenreXrefEntity;
import java.util.LinkedList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GameGenreXrefRepository extends JpaRepository<GameGenreXrefEntity, Integer> {

    @Query("SELECT gg.game FROM GameGenreXrefEntity gg WHERE gg.genre.id = :genreId")
    LinkedList<GameEntity> findGamesByGenre(int genreId);

    @Query("SELECT gg.genre FROM GameGenreXrefEntity gg WHERE gg.game.id = :gameId")
    LinkedList<GameGenreEntity> findGenresByGame(int gameId);

    @Query("SELECT gg FROM GameGenreXrefEntity gg WHERE gg.game.id = :gameId")
    LinkedList<GameGenreXrefEntity> findGameGenreReferencesByGame(int gameId);
}
