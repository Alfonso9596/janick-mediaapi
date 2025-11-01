package com.janick_mediadb.janick_mediaapi.repository;

import com.janick_mediadb.janick_mediaapi.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameRepository extends JpaRepository<GameEntity, Integer>, JpaSpecificationExecutor<GameEntity> {

    @Query("SELECT g.name FROM GameEntity g")
    public List<String> getAllGameNames();
}
