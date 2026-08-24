package com.janickmediadb.janickmediaapi.repository;

import com.janickmediadb.janickmediaapi.entity.MovieGenreEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface MovieGenreRepository extends JpaRepository<MovieGenreEntity, Integer>, JpaSpecificationExecutor<MovieGenreEntity> {

    Optional<MovieGenreEntity> findByName(String name);

    @Query("SELECT m FROM MovieGenreEntity m ORDER BY name")
    List<MovieGenreEntity> findAllOrderByName();
}
