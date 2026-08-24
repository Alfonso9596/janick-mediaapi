package com.janickmediadb.janickmediaapi.repository;

import com.janickmediadb.janickmediaapi.entity.GamePlatformEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface GamePlatformRepository extends JpaRepository<GamePlatformEntity, Integer>, JpaSpecificationExecutor<GamePlatformEntity> {

    Optional<GamePlatformEntity> findByName(String name);

    @Query("SELECT g FROM GamePlatformEntity g ORDER BY name")
    List<GamePlatformEntity> findAllOrderByName();
}
