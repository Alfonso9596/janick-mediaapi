package com.janickmediadb.janickmediaapi.repository;

import com.janickmediadb.janickmediaapi.entity.MovieEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface MovieRepository extends JpaRepository<MovieEntity, Integer>, JpaSpecificationExecutor<MovieEntity> {

    @Query("SELECT m.name FROM MovieEntity m")
    List<String> getAllMovieNames();
}
