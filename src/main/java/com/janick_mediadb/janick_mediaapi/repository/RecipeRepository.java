package com.janick_mediadb.janick_mediaapi.repository;

import com.janick_mediadb.janick_mediaapi.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecipeRepository extends JpaRepository<RecipeEntity, Integer>, JpaSpecificationExecutor<RecipeEntity> {

    @Query("SELECT r.name FROM RecipeEntity r")
    List<String> getAllRecipeNames();
}
