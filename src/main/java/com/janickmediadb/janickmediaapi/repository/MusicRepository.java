package com.janickmediadb.janickmediaapi.repository;

import com.janickmediadb.janickmediaapi.entity.MusicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MusicRepository extends JpaRepository<MusicEntity, Integer>, JpaSpecificationExecutor<MusicEntity> {
}
