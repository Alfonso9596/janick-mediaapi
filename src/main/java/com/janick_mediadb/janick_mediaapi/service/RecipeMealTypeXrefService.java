package com.janick_mediadb.janick_mediaapi.service;

import com.janick_mediadb.janick_mediaapi.entity.MealTypeEntity;
import com.janick_mediadb.janick_mediaapi.entity.RecipeEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.RecipeMealTypeXrefEntity;
import com.janick_mediadb.janick_mediaapi.model.RecipeModel;
import com.janick_mediadb.janick_mediaapi.repository.xref.RecipeMealTypeXrefRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeMealTypeXrefService {

    private final RecipeMealTypeXrefRepository recipeMealTypeXrefRepository;

    @Autowired
    public RecipeMealTypeXrefService(RecipeMealTypeXrefRepository recipeMealTypeXrefRepository) {
        this.recipeMealTypeXrefRepository = recipeMealTypeXrefRepository;
    }

    public List<RecipeEntity> findRecipesByMealType(int mealTypeId) {
        return recipeMealTypeXrefRepository.findRecipesByMealType(mealTypeId);
    }

    public void deleteMealTypeReferenceByRecipeId(int recipeId) {
        List<RecipeMealTypeXrefEntity> rms = recipeMealTypeXrefRepository.findMealTypeReferencesByRecipe(recipeId);
        if (!rms.isEmpty()) {
            recipeMealTypeXrefRepository.deleteAll(rms);
        }
    }

    public void saveRecipeMealTypeXref(RecipeEntity recipe, List<MealTypeEntity> mealTypes) {
        if (!mealTypes.isEmpty()) {
            for (MealTypeEntity mealType : mealTypes) {
                RecipeMealTypeXrefEntity rm = new RecipeMealTypeXrefEntity();
                rm.setRecipe(recipe);
                rm.setMealType(mealType);
                recipeMealTypeXrefRepository.save(rm);
            }
        }
    }

    public void collectMealTypes(int recipeId, RecipeModel recipeModel) {
        List<MealTypeEntity> mealTypes = recipeMealTypeXrefRepository.findMealTypesByRecipe(recipeId);
        recipeModel.setMealTypes(MealTypeEntity.toModels(mealTypes));
    }
}
