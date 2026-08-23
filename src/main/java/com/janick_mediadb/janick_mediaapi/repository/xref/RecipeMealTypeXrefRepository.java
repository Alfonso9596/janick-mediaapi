package com.janick_mediadb.janick_mediaapi.repository.xref;

import com.janick_mediadb.janick_mediaapi.entity.MealTypeEntity;
import com.janick_mediadb.janick_mediaapi.entity.RecipeEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.RecipeMealTypeXrefEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.LinkedList;

public interface RecipeMealTypeXrefRepository extends JpaRepository<RecipeMealTypeXrefEntity, Integer> {

    @Query("SELECT rm.recipe FROM RecipeMealTypeXrefEntity rm WHERE rm.mealType.id = :mealTypeId")
    LinkedList<RecipeEntity> findRecipesByMealType(int mealTypeId);

    @Query("SELECT rm.mealType FROM RecipeMealTypeXrefEntity rm WHERE rm.recipe.id = :recipeId")
    LinkedList<MealTypeEntity> findMealTypesByRecipe(int recipeId);

    @Query("SELECT rm FROM RecipeMealTypeXrefEntity rm WHERE rm.recipe.id = :recipeId")
    LinkedList<RecipeMealTypeXrefEntity> findMealTypeReferencesByRecipe(int recipeId);
}
