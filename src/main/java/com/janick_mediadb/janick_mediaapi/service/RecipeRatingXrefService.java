package com.janick_mediadb.janick_mediaapi.service;

import com.janick_mediadb.janick_mediaapi.entity.RecipeEntity;
import com.janick_mediadb.janick_mediaapi.entity.security.UsersEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.RecipeRatingXrefEntity;
import com.janick_mediadb.janick_mediaapi.model.RecipeModel;
import com.janick_mediadb.janick_mediaapi.repository.xref.RecipeRatingXrefRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeRatingXrefService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeRatingXrefService.class);

    private final RecipeRatingXrefRepository recipeRatingXrefRepository;

    @Autowired
    public RecipeRatingXrefService(RecipeRatingXrefRepository recipeRatingXrefRepository) {
        this.recipeRatingXrefRepository = recipeRatingXrefRepository;
    }

    public void collectRatings(int recipeId, RecipeModel model) {
        List<RecipeRatingXrefEntity> ratings = recipeRatingXrefRepository.findAllByRecipeId(recipeId);
        if (!ratings.isEmpty()) {
            model.setRatingAmount(ratings.size());
            double result = (double) (ratings.stream().mapToInt(RecipeRatingXrefEntity::getRating).sum()) / ratings.size();
            model.setRatingValue(result);
        } else {
            model.setRatingAmount(0);
            model.setRatingValue(0);
        }
    }

    public Optional<RecipeRatingXrefEntity> findByRecipeIdAndUserId(int recipeId, int userId) {
        return recipeRatingXrefRepository.findByRecipeIdAndUserId(recipeId, userId);
    }

    public void deleteRecipeRatingReferenceByRecipeId(int recipeId) {
        List<RecipeRatingXrefEntity> ratings = recipeRatingXrefRepository.findAllByRecipeId(recipeId);
        if (!ratings.isEmpty()) {
            recipeRatingXrefRepository.deleteAll(ratings);
        }
    }

    public RecipeRatingXrefEntity addRating(RecipeEntity recipe, UsersEntity user, int rating) {
        RecipeRatingXrefEntity recipeRatingXrefEntity = new RecipeRatingXrefEntity();
        recipeRatingXrefEntity.setRecipe(recipe);
        recipeRatingXrefEntity.setUser(user);
        recipeRatingXrefEntity.setRating(rating);

        LOGGER.info("addRating: Saving recipe rating {} for {}", rating, recipe.getName());
        return recipeRatingXrefRepository.save(recipeRatingXrefEntity);
    }

    public RecipeRatingXrefEntity updateRating(RecipeRatingXrefEntity recipeRating, int rating) {
        recipeRating.setRating(rating);
        return recipeRatingXrefRepository.save(recipeRating);
    }
}
