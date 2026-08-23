package com.janick_mediadb.janick_mediaapi.repository;

import com.janick_mediadb.janick_mediaapi.entity.MealTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MealTypeRepository extends JpaRepository<MealTypeEntity, Integer>, JpaSpecificationExecutor<MealTypeEntity> {

    Optional<MealTypeEntity> findByName(String name);

    @Query("SELECT m FROM MealTypeEntity m ORDER BY name")
    List<MealTypeEntity> findAllOrderByName();
}
