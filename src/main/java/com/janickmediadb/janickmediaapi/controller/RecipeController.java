package com.janickmediadb.janickmediaapi.controller;

import com.janickmediadb.janickmediaapi.input.RecipeInput;
import com.janickmediadb.janickmediaapi.model.FileInfoModel;
import com.janickmediadb.janickmediaapi.model.RatingUpdateModel;
import com.janickmediadb.janickmediaapi.model.RecipeModel;
import com.janickmediadb.janickmediaapi.model.response.RecipeResponse;
import com.janickmediadb.janickmediaapi.model.response.RecipeSearchCriteria;
import com.janickmediadb.janickmediaapi.service.RecipeService;
import com.janickmediadb.janickmediaapi.utils.AppConstants;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
