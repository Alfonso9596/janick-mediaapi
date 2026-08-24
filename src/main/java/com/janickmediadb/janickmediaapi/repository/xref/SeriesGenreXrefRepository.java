package com.janickmediadb.janickmediaapi.repository.xref;

import com.janickmediadb.janickmediaapi.entity.MovieGenreEntity;
import com.janickmediadb.janickmediaapi.entity.SeriesEntity;
import com.janickmediadb.janickmediaapi.entity.xref.SeriesGenreXrefEntity;
import java.util.LinkedList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SeriesGenreXrefRepository extends JpaRepository<SeriesGenreXrefEntity, Integer> {

    @Query("SELECT sg.series FROM SeriesGenreXrefEntity sg WHERE sg.genre.id = :genreId")
    LinkedList<SeriesEntity> findSeriesByGenre(int genreId);

    @Query("SELECT sg.genre FROM SeriesGenreXrefEntity sg WHERE sg.series.id = :seriesId")
    LinkedList<MovieGenreEntity> findGenresBySeries(int seriesId);

    @Query("SELECT sg FROM SeriesGenreXrefEntity sg WHERE sg.series.id = :seriesId")
    LinkedList<SeriesGenreXrefEntity> findSeriesGenreReferenceBySeries(int seriesId);
}
