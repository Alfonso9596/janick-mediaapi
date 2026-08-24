package com.janickmediadb.janickmediaapi.model.specifications;

import com.janickmediadb.janickmediaapi.entity.MovieEntity;
import com.janickmediadb.janickmediaapi.entity.MovieGenreEntity;
import com.janickmediadb.janickmediaapi.entity.xref.MovieGenreXrefEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class MovieSpecification {

    private MovieSpecification() {
        throw new IllegalStateException("Non-constructor class");
    }

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
            return criteriaBuilder.like(movieGenreJoin.get("name"), genre);
        };
    }
}
