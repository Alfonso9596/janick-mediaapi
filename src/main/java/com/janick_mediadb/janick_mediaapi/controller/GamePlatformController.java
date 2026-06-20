package com.janick_mediadb.janick_mediaapi.controller;

import com.janick_mediadb.janick_mediaapi.input.GamePlatformInput;
import com.janick_mediadb.janick_mediaapi.model.GamePlatformModel;
import com.janick_mediadb.janick_mediaapi.model.response.GamePlatformResponse;
import com.janick_mediadb.janick_mediaapi.model.response.GamePlatformSearchCriteria;
import com.janick_mediadb.janick_mediaapi.service.GamePlatformService;
import com.janick_mediadb.janick_mediaapi.utils.AppConstants;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platforms")
public class GamePlatformController {

    private final GamePlatformService gamePlatformService;

    @Autowired
    public  GamePlatformController(GamePlatformService gamePlatformService) {
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
