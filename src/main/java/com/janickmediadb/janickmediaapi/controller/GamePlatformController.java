package com.janickmediadb.janickmediaapi.controller;

import com.janickmediadb.janickmediaapi.input.GamePlatformInput;
import com.janickmediadb.janickmediaapi.model.GamePlatformModel;
import com.janickmediadb.janickmediaapi.model.response.GamePlatformResponse;
import com.janickmediadb.janickmediaapi.model.response.GamePlatformSearchCriteria;
import com.janickmediadb.janickmediaapi.service.GamePlatformService;
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
@RequestMapping("/api/platforms")
public class GamePlatformController {

    private final GamePlatformService gamePlatformService;

    @Autowired
    public GamePlatformController(GamePlatformService gamePlatformService) {
        this.gamePlatformService = gamePlatformService;
    }

    @GetMapping
    public GamePlatformResponse getPageablePlatforms(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE, required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @ParameterObject GamePlatformSearchCriteria criteria) {
        return gamePlatformService.getPageablePlatforms(page, pageSize, sortBy, sortDir, criteria);
    }

    @GetMapping("/list")
    public ResponseEntity<List<GamePlatformModel>> getAllPlatforms() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gamePlatformService.getAllPlatforms());
    }

    @PostMapping
    public ResponseEntity<GamePlatformModel> savePlatform(
            @RequestBody GamePlatformInput gamePlatformInput) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gamePlatformService.savePlatform(gamePlatformInput));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePlatform(@PathVariable("id") int id, @RequestBody GamePlatformInput gamePlatformInput) {
        return gamePlatformService.updatePlatform(id, gamePlatformInput);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlatform(@PathVariable("id") int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gamePlatformService.deletePlatform(id));
    }
}
