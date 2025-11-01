package com.janick_mediadb.janick_mediaapi.repository.xref;

import com.janick_mediadb.janick_mediaapi.entity.MovieGenreEntity;
import com.janick_mediadb.janick_mediaapi.entity.MovieEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.MovieGenreXrefEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.LinkedList;

public interface MovieGenreXrefRepository extends JpaRepository<MovieGenreXrefEntity, Integer> {

    @Query("SELECT mg.movie FROM MovieGenreXrefEntity mg WHERE mg.genre.id = :genreId")
    LinkedList<MovieEntity> findMoviesByGenre(int genreId);

    @Query("SELECT mg.genre FROM MovieGenreXrefEntity mg WHERE mg.movie.id = :movieId")
    LinkedList<MovieGenreEntity> findGenresByMovie(int movieId);

    @Query("SELECT mg FROM MovieGenreXrefEntity mg WHERE mg.movie.id = :movieId")
    LinkedList<MovieGenreXrefEntity> findMovieGenreReferencesByMovie(int movieId);
}
