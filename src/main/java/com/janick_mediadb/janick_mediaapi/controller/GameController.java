package com.janick_mediadb.janick_mediaapi.controller;

import com.janick_mediadb.janick_mediaapi.input.GameInput;
import com.janick_mediadb.janick_mediaapi.model.GameModel;
import com.janick_mediadb.janick_mediaapi.model.response.GameResponse;
import com.janick_mediadb.janick_mediaapi.model.response.GameSearchCriteria;
import com.janick_mediadb.janick_mediaapi.service.GameService;
import com.janick_mediadb.janick_mediaapi.utils.AppConstants;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping
    public GameResponse getAllGames(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE, required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @ParameterObject GameSearchCriteria criteria) {
        return gameService.getAllGames(page, pageSize, sortBy, sortDir, criteria);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameModel> getGameById(
            @PathVariable("id") int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gameService.getGameById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGame(
            @PathVariable("id") int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gameService.deleteGame(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameModel> saveGame(
            @RequestBody GameInput gameInput) {

        GameModel gameModel = gameService.saveGame(gameInput);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gameModel);
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<String> rateGame(
            @PathVariable("id") int id,
            @RequestParam("rating") int rating) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gameService.rateGame(id, rating));
    }
}
