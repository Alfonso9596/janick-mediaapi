package com.janickmediadb.janickmediaapi.repository;

import com.janickmediadb.janickmediaapi.entity.GameEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface GameRepository extends JpaRepository<GameEntity, Integer>, JpaSpecificationExecutor<GameEntity> {

    @Query("SELECT g.name FROM GameEntity g")
    List<String> getAllGameNames();
}
