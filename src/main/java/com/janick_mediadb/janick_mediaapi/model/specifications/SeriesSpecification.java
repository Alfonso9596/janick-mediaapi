package com.janick_mediadb.janick_mediaapi.model.specifications;

import com.janick_mediadb.janick_mediaapi.entity.MovieGenreEntity;
import com.janick_mediadb.janick_mediaapi.entity.SeriesEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.SeriesGenreXrefEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class SeriesSpecification {

    public static Specification<SeriesEntity> likeName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<SeriesEntity> containsGenre(String genre) {
        return (root, query, criteriaBuilder) -> {
            Join<SeriesEntity, SeriesGenreXrefEntity> seriesGenreXrefJoin = root.join("seriesGenreXrefs", JoinType.INNER);
            Join<SeriesGenreXrefEntity, MovieGenreEntity> movieGenreJoin = seriesGenreXrefJoin.join("genre", JoinType.INNER);
            return criteriaBuilder.like(movieGenreJoin.get("name"), "%" + genre + "%");
        };
    }
}
