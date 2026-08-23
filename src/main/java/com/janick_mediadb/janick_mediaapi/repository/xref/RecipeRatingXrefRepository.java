package com.janick_mediadb.janick_mediaapi.repository.xref;

import com.janick_mediadb.janick_mediaapi.entity.xref.RecipeRatingXrefEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.LinkedList;
import java.util.Optional;

public interface RecipeRatingXrefRepository extends JpaRepository<RecipeRatingXrefEntity, Integer> {

    LinkedList<RecipeRatingXrefEntity> findAllByRecipeId(int recipeId);

    Optional<RecipeRatingXrefEntity> findByRecipeIdAndUserId(int recipeId, int userId);
}
