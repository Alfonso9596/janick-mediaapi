package com.janick_mediadb.janick_mediaapi.repository.xref;

import com.janick_mediadb.janick_mediaapi.entity.xref.MovieRatingXrefEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.LinkedList;
import java.util.Optional;

public interface MovieRatingXrefRepository extends JpaRepository<MovieRatingXrefEntity, Integer> {

    LinkedList<MovieRatingXrefEntity> findAllByMovieId(int movieId);

    Optional<MovieRatingXrefEntity> findByMovieIdAndUserId(int movieId, int userId);
}
