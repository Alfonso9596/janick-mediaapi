package com.janick_mediadb.janick_mediaapi.repository;

import com.janick_mediadb.janick_mediaapi.entity.GameGenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface GameGenreRepository extends JpaRepository<GameGenreEntity, Integer>, JpaSpecificationExecutor<GameGenreEntity> {

    Optional<GameGenreEntity> findByName(String name);
}
