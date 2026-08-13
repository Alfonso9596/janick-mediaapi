package com.janick_mediadb.janick_mediaapi.repository.xref;

import com.janick_mediadb.janick_mediaapi.entity.MusicEntity;
import com.janick_mediadb.janick_mediaapi.entity.MusicGenreEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.MusicGenreXrefEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.LinkedList;

public interface MusicGenreXrefRepository extends JpaRepository<MusicGenreXrefEntity, Integer> {

    @Query("SELECT mg.music FROM MusicGenreXrefEntity mg WHERE mg.genre.id = :genreId")
    LinkedList<MusicEntity> findMusicByGenre(int genreId);

    @Query("SELECT mg.genre FROM MusicGenreXrefEntity mg WHERE mg.music.id = :musicId")
    LinkedList<MusicGenreEntity> findGenresByMusic(int musicId);

    @Query("SELECT mg FROM MusicGenreXrefEntity mg WHERE mg.music.id = :musicId")
    LinkedList<MusicGenreXrefEntity> findMusicGenreReferencesByMusic(int musicId);
}
