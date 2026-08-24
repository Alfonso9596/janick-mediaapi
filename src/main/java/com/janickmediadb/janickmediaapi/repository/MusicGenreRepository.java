package com.janickmediadb.janickmediaapi.repository;

import com.janickmediadb.janickmediaapi.entity.MusicGenreEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MusicGenreRepository extends JpaRepository<MusicGenreEntity, Integer>, JpaSpecificationExecutor<MusicGenreEntity> {

    Optional<MusicGenreEntity> findByName(String name);
}
