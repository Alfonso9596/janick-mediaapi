package com.janickmediadb.janickmediaapi.model.specifications;

import com.janickmediadb.janickmediaapi.entity.MusicEntity;
import com.janickmediadb.janickmediaapi.entity.MusicGenreEntity;
import com.janickmediadb.janickmediaapi.entity.xref.MusicGenreXrefEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class MusicSpecification {

    private MusicSpecification() {
        throw new IllegalStateException("Non-constructor class");
    }

    public static Specification<MusicEntity> likeName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name").as(String.class), "%" + name + "%");
    }

    public static Specification<MusicEntity> likeArtist(String artist) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("artist").as(String.class), "%" + artist + "%");
    }

    public static Specification<MusicEntity> equalsYear(int year) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("year"), year);
    }

    public static Specification<MusicEntity> containsGenre(String genre) {
        return (root, query, criteriaBuilder) -> {
            Join<MusicEntity, MusicGenreXrefEntity> musicGenreXrefJoin = root.join("musicGenreXrefs", JoinType.INNER);
            Join<MusicGenreXrefEntity, MusicGenreEntity> musicGenreJoin = musicGenreXrefJoin.join("genre", JoinType.INNER);
            return criteriaBuilder.like(musicGenreJoin.get("name"), "%" + genre + "%");
        };
    }
}
