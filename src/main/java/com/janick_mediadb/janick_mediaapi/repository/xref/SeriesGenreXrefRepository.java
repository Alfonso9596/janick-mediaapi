package com.janick_mediadb.janick_mediaapi.repository.xref;

import com.janick_mediadb.janick_mediaapi.entity.MovieGenreEntity;
import com.janick_mediadb.janick_mediaapi.entity.SeriesEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.SeriesGenreXrefEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.LinkedList;

public interface SeriesGenreXrefRepository extends JpaRepository<SeriesGenreXrefEntity, Integer> {

    @Query("SELECT sg.series FROM SeriesGenreXrefEntity sg WHERE sg.genre.id = :genreId")
    LinkedList<SeriesEntity> findSeriesByGenre(int genreId);

    @Query("SELECT sg.genre FROM SeriesGenreXrefEntity sg WHERE sg.series.id = :seriesId")
    LinkedList<MovieGenreEntity> findGenresBySeries(int seriesId);

    @Query("SELECT sg FROM SeriesGenreXrefEntity sg WHERE sg.series.id = :seriesId")
    LinkedList<SeriesGenreXrefEntity> findSeriesGenreReferenceBySeries(int seriesId);
}
