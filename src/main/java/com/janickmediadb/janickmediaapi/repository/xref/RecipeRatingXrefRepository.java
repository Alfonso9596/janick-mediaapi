package com.janickmediadb.janickmediaapi.repository.xref;

import com.janickmediadb.janickmediaapi.entity.xref.RecipeRatingXrefEntity;
import java.util.LinkedList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRatingXrefRepository extends JpaRepository<RecipeRatingXrefEntity, Integer> {

    LinkedList<RecipeRatingXrefEntity> findAllByRecipeId(int recipeId);

    Optional<RecipeRatingXrefEntity> findByRecipeIdAndUserId(int recipeId, int userId);
}
