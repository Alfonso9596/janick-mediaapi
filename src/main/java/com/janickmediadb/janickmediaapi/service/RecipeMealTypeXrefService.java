package com.janickmediadb.janickmediaapi.service;

import com.janickmediadb.janickmediaapi.entity.MealTypeEntity;
import com.janickmediadb.janickmediaapi.entity.RecipeEntity;
import com.janickmediadb.janickmediaapi.entity.xref.RecipeMealTypeXrefEntity;
import com.janickmediadb.janickmediaapi.model.RecipeModel;
import com.janickmediadb.janickmediaapi.repository.xref.RecipeMealTypeXrefRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
