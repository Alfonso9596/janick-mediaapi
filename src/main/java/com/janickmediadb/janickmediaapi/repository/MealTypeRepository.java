package com.janickmediadb.janickmediaapi.repository;

import com.janickmediadb.janickmediaapi.entity.MealTypeEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface MealTypeRepository extends JpaRepository<MealTypeEntity, Integer>, JpaSpecificationExecutor<MealTypeEntity> {

    Optional<MealTypeEntity> findByName(String name);

    @Query("SELECT m FROM MealTypeEntity m ORDER BY name")
    List<MealTypeEntity> findAllOrderByName();
}
