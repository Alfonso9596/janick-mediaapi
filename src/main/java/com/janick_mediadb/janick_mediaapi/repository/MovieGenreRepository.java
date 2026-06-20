package com.janick_mediadb.janick_mediaapi.repository;

import com.janick_mediadb.janick_mediaapi.entity.MovieGenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MovieGenreRepository extends JpaRepository<MovieGenreEntity, Integer>, JpaSpecificationExecutor<MovieGenreEntity> {

    Optional<MovieGenreEntity> findByName(String name);

    @Query("SELECT m FROM MovieGenreEntity m ORDER BY name")
    List<MovieGenreEntity> findAllOrderByName();
}
