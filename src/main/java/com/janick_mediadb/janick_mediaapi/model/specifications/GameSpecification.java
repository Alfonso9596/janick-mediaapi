package com.janick_mediadb.janick_mediaapi.model.specifications;

import com.janick_mediadb.janick_mediaapi.entity.GameEntity;
import com.janick_mediadb.janick_mediaapi.entity.GameGenreEntity;
import com.janick_mediadb.janick_mediaapi.entity.GamePlatformEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.GameGenreXrefEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.GamePlatformXrefEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class GameSpecification {

    private GameSpecification() {
        throw new IllegalStateException("Non-constructor class");
    }

    public static Specification<GameEntity> likeName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<GameEntity> equalsYear(int year) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("year"), year);
    }

    public static Specification<GameEntity> containsGenre(String genre) {
        return (root, query, criteriaBuilder) -> {
            Join<GameEntity, GameGenreXrefEntity> gameGenreXrefJoin = root.join("gameGenreXrefs", JoinType.INNER);
            Join<GameGenreXrefEntity, GameGenreEntity> gameGenreJoin = gameGenreXrefJoin.join("genre", JoinType.INNER);
            return criteriaBuilder.like(gameGenreJoin.get("name"), "%" + genre + "%");
        };
    }

    public static Specification<GameEntity> containsPlatform(String platform) {
        return (root, query, criteriaBuilder) -> {
            Join<GameEntity, GamePlatformXrefEntity> gamePlatformXrefJoin = root.join("gamePlatformXrefs", JoinType.INNER);
            Join<GamePlatformXrefEntity, GamePlatformEntity> gamePlatformJoin = gamePlatformXrefJoin.join("platform", JoinType.INNER);
            return criteriaBuilder.like(gamePlatformJoin.get("name"), "%" + platform + "%");
        };
    }
}
