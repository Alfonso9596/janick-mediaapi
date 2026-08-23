package com.janick_mediadb.janick_mediaapi.controller;

import com.janick_mediadb.janick_mediaapi.input.RecipeInput;
import com.janick_mediadb.janick_mediaapi.model.FileInfoModel;
import com.janick_mediadb.janick_mediaapi.model.RatingUpdateModel;
import com.janick_mediadb.janick_mediaapi.model.RecipeModel;
import com.janick_mediadb.janick_mediaapi.model.response.RecipeResponse;
import com.janick_mediadb.janick_mediaapi.model.response.RecipeSearchCriteria;
import com.janick_mediadb.janick_mediaapi.service.RecipeService;
import com.janick_mediadb.janick_mediaapi.utils.AppConstants;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public RecipeResponse getAllRecipes(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE, required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @ParameterObject RecipeSearchCriteria criteria) {
        return recipeService.getAllRecipes(page, pageSize, sortBy, sortDir, criteria);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeModel> getRecipeById(
            @PathVariable("id") int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(recipeService.getRecipeById(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipeModel> saveRecipe(
            @RequestBody RecipeInput recipeInput) {

        RecipeModel recipeModel = recipeService.saveRecipe(recipeInput);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(recipeModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecipe(
            @PathVariable("id") int id) {
        return recipeService.deleteRecipe(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeModel> updateRecipe(
            @PathVariable("id") int id,
            @RequestBody RecipeInput recipeInput) {

        RecipeModel recipeModel = recipeService.updateRecipe(id, recipeInput);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(recipeModel);
    }

    @PostMapping("/rating")
    public ResponseEntity<String> rateRecipe(
            @RequestBody RatingUpdateModel ratingUpdateModel) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(recipeService.rateRecipe(ratingUpdateModel));
    }

    @GetMapping("/{id}/files")
    public ResponseEntity<List<FileInfoModel>> getRecipeFiles(
            @PathVariable("id") int id) {
        return recipeService.getRecipeFiles(id);
    }
}
