package com.janick_mediadb.janick_mediaapi.model.specifications;

import com.janick_mediadb.janick_mediaapi.entity.MovieEntity;
import com.janick_mediadb.janick_mediaapi.entity.MovieGenreEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.MovieGenreXrefEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class MovieSpecification {

    public static Specification<MovieEntity> likeName(String name) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<MovieEntity> equalsYear(int year) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("year"), year);
    }

    public static Specification<MovieEntity> containsGenre(String genre) {
        return (root, query, criteriaBuilder) -> {
            Join<MovieEntity, MovieGenreXrefEntity> movieGenreXrefJoin = root.join("movieGenreXrefs", JoinType.INNER);
            Join<MovieGenreXrefEntity, MovieGenreEntity> movieGenreJoin = movieGenreXrefJoin.join("genre", JoinType.LEFT);
            return criteriaBuilder.like(movieGenreJoin.get("name"), "%" + genre + "%");
        };
    }
}
