package com.janick_mediadb.janick_mediaapi.repository;

import com.janick_mediadb.janick_mediaapi.entity.GamePlatformEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GamePlatformRepository extends JpaRepository<GamePlatformEntity, Integer>, JpaSpecificationExecutor<GamePlatformEntity> {

    Optional<GamePlatformEntity> findByName(String name);

    @Query("SELECT g FROM GamePlatformEntity g ORDER BY name")
    List<GamePlatformEntity> findAllOrderByName();
}
