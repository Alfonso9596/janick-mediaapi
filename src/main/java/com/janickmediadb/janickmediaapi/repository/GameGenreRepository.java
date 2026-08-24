package com.janickmediadb.janickmediaapi.repository;

import com.janickmediadb.janickmediaapi.entity.GameGenreEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GameGenreRepository extends JpaRepository<GameGenreEntity, Integer>, JpaSpecificationExecutor<GameGenreEntity> {

    Optional<GameGenreEntity> findByName(String name);
}
