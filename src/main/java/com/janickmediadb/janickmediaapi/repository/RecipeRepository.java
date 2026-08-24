package com.janickmediadb.janickmediaapi.repository;

import com.janickmediadb.janickmediaapi.entity.RecipeEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface RecipeRepository extends JpaRepository<RecipeEntity, Integer>, JpaSpecificationExecutor<RecipeEntity> {

    @Query("SELECT r.name FROM RecipeEntity r")
    List<String> getAllRecipeNames();
}
