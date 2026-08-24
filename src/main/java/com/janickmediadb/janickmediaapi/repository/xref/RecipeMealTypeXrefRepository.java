package com.janickmediadb.janickmediaapi.repository.xref;

import com.janickmediadb.janickmediaapi.entity.MealTypeEntity;
import com.janickmediadb.janickmediaapi.entity.RecipeEntity;
import com.janickmediadb.janickmediaapi.entity.xref.RecipeMealTypeXrefEntity;
import java.util.LinkedList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecipeMealTypeXrefRepository extends JpaRepository<RecipeMealTypeXrefEntity, Integer> {

    @Query("SELECT rm.recipe FROM RecipeMealTypeXrefEntity rm WHERE rm.mealType.id = :mealTypeId")
    LinkedList<RecipeEntity> findRecipesByMealType(int mealTypeId);

    @Query("SELECT rm.mealType FROM RecipeMealTypeXrefEntity rm WHERE rm.recipe.id = :recipeId")
    LinkedList<MealTypeEntity> findMealTypesByRecipe(int recipeId);

    @Query("SELECT rm FROM RecipeMealTypeXrefEntity rm WHERE rm.recipe.id = :recipeId")
    LinkedList<RecipeMealTypeXrefEntity> findMealTypeReferencesByRecipe(int recipeId);
}
