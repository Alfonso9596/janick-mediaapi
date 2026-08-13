package com.janick_mediadb.janick_mediaapi.repository;

import com.janick_mediadb.janick_mediaapi.entity.MusicGenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface MusicGenreRepository extends JpaRepository<MusicGenreEntity, Integer>, JpaSpecificationExecutor<MusicGenreEntity> {

    Optional<MusicGenreEntity> findByName(String name);
}
