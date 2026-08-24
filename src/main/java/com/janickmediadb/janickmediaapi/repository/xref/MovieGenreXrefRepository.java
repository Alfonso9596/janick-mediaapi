package com.janickmediadb.janickmediaapi.repository.xref;

import com.janickmediadb.janickmediaapi.entity.MovieEntity;
import com.janickmediadb.janickmediaapi.entity.MovieGenreEntity;
import com.janickmediadb.janickmediaapi.entity.xref.MovieGenreXrefEntity;
import java.util.LinkedList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MovieGenreXrefRepository extends JpaRepository<MovieGenreXrefEntity, Integer> {

    @Query("SELECT mg.movie FROM MovieGenreXrefEntity mg WHERE mg.genre.id = :genreId")
    LinkedList<MovieEntity> findMoviesByGenre(int genreId);

    @Query("SELECT mg.genre FROM MovieGenreXrefEntity mg WHERE mg.movie.id = :movieId")
    LinkedList<MovieGenreEntity> findGenresByMovie(int movieId);

    @Query("SELECT mg FROM MovieGenreXrefEntity mg WHERE mg.movie.id = :movieId")
    LinkedList<MovieGenreXrefEntity> findMovieGenreReferencesByMovie(int movieId);
}
