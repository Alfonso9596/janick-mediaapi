package com.janickmediadb.janickmediaapi.controller;

import com.janickmediadb.janickmediaapi.input.MealTypeInput;
import com.janickmediadb.janickmediaapi.model.MealTypeModel;
import com.janickmediadb.janickmediaapi.model.response.MealTypeResponse;
import com.janickmediadb.janickmediaapi.model.response.MealTypeSearchCriteria;
import com.janickmediadb.janickmediaapi.service.MealTypeService;
import com.janickmediadb.janickmediaapi.utils.AppConstants;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/mealTypes")
public class MealTypeController {

    private final MealTypeService mealTypeService;

    @Autowired
    public MealTypeController(MealTypeService mealTypeService) {
        this.mealTypeService = mealTypeService;
    }

    @GetMapping
    public MealTypeResponse getPageableMealTypes(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE, required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @ParameterObject MealTypeSearchCriteria criteria) {
        return mealTypeService.getPageableMealTypes(page, pageSize, sortBy, sortDir, criteria);
    }

    @GetMapping("/list")
    public ResponseEntity<List<MealTypeModel>> getAllMealTypes() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mealTypeService.getAllMealTypes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MealTypeModel> getMealTypeById(
            @PathVariable("id") int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mealTypeService.getMealTypeById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMealType(
            @PathVariable("id") int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mealTypeService.deleteMealType(id));
    }

    @PostMapping
    public ResponseEntity<MealTypeModel> saveMealType(
            @RequestBody MealTypeInput mealTypeInput) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mealTypeService.saveMealType(mealTypeInput));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateMealType(
            @PathVariable("id") int id,
            @RequestBody MealTypeInput mealTypeInput) {
        return mealTypeService.updateMealType(id, mealTypeInput);
    }
}
