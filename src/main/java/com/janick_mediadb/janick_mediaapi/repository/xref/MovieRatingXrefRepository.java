package com.janick_mediadb.janick_mediaapi.repository.xref;

import com.janick_mediadb.janick_mediaapi.entity.xref.MovieRatingXrefEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.LinkedList;

public interface MovieRatingXrefRepository extends JpaRepository<MovieRatingXrefEntity, Integer> {

    LinkedList<MovieRatingXrefEntity> findAllByMovieId(int movieId);
}
