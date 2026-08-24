package com.janickmediadb.janickmediaapi.repository.xref;

import com.janickmediadb.janickmediaapi.entity.xref.MovieRatingXrefEntity;
import java.util.LinkedList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRatingXrefRepository extends JpaRepository<MovieRatingXrefEntity, Integer> {

    LinkedList<MovieRatingXrefEntity> findAllByMovieId(int movieId);

    Optional<MovieRatingXrefEntity> findByMovieIdAndUserId(int movieId, int userId);
}
