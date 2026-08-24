package com.janickmediadb.janickmediaapi.repository.xref;

import com.janickmediadb.janickmediaapi.entity.MusicEntity;
import com.janickmediadb.janickmediaapi.entity.MusicGenreEntity;
import com.janickmediadb.janickmediaapi.entity.xref.MusicGenreXrefEntity;
import java.util.LinkedList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MusicGenreXrefRepository extends JpaRepository<MusicGenreXrefEntity, Integer> {

    @Query("SELECT mg.music FROM MusicGenreXrefEntity mg WHERE mg.genre.id = :genreId")
    LinkedList<MusicEntity> findMusicByGenre(int genreId);

    @Query("SELECT mg.genre FROM MusicGenreXrefEntity mg WHERE mg.music.id = :musicId")
    LinkedList<MusicGenreEntity> findGenresByMusic(int musicId);

    @Query("SELECT mg FROM MusicGenreXrefEntity mg WHERE mg.music.id = :musicId")
    LinkedList<MusicGenreXrefEntity> findMusicGenreReferencesByMusic(int musicId);
}
