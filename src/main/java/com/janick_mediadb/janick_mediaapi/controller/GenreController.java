package com.janick_mediadb.janick_mediaapi.controller;

import com.janick_mediadb.janick_mediaapi.input.MovieGenreInput;
import com.janick_mediadb.janick_mediaapi.model.MovieGenreModel;
import com.janick_mediadb.janick_mediaapi.model.response.GenreResponse;
import com.janick_mediadb.janick_mediaapi.model.response.GenreSearchCriteria;
import com.janick_mediadb.janick_mediaapi.service.GameGenreService;
import com.janick_mediadb.janick_mediaapi.service.MovieGenreService;
import com.janick_mediadb.janick_mediaapi.utils.AppConstants;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    private final MovieGenreService movieGenreService;

    private final GameGenreService gameGenreService;

    @Autowired
    public GenreController(MovieGenreService movieGenreService, GameGenreService gameGenreService) {
        this.movieGenreService = movieGenreService;
        this.gameGenreService = gameGenreService;
    }

    @GetMapping("/movies")
    public GenreResponse getPageableMovieGenres(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE, required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @ParameterObject GenreSearchCriteria criteria) {
        return movieGenreService.getPageableGenres(page, pageSize, sortBy, sortDir, criteria);
    }

    @GetMapping("/movies/list")
    public ResponseEntity<List<MovieGenreModel>> getAllMovieGenres() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(movieGenreService.getAllGenres());
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<MovieGenreModel> getMovieGenreById(
            @PathVariable("id") int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(movieGenreService.getGenreById(id));
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<String> deleteMovieGenre(
            @PathVariable("id") int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(movieGenreService.deleteGenre(id));
    }

    @PostMapping("/movies")
    public ResponseEntity<MovieGenreModel> saveMovieGenre(
            @RequestBody MovieGenreInput movieGenreInput) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(movieGenreService.saveGenre(movieGenreInput));
    }

    @PutMapping("/movies/{id}")
    public ResponseEntity<String> updateMovieGenre(@PathVariable("id") int id, @RequestBody MovieGenreInput movieGenreInput) {
        return movieGenreService.updateGenre(id, movieGenreInput);
    }

    @GetMapping("/games")
    public GenreResponse getPageableGameGenres(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE, required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @ParameterObject GenreSearchCriteria criteria) {
        return gameGenreService.getPageableGenres(page, pageSize, sortBy, sortDir, criteria);
    }

    @GetMapping("/games/list")
    public ResponseEntity<List<MovieGenreModel>> getAllGameGenres() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gameGenreService.getAllGenres());
    }

    @GetMapping("/games/{id}")
    public ResponseEntity<MovieGenreModel> getGameGenreById(
            @PathVariable("id") int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gameGenreService.getGenreById(id));
    }

    @DeleteMapping("/games/{id}")
    public ResponseEntity<String> deleteGameGenre(
            @PathVariable("id") int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gameGenreService.deleteGenre(id));
    }

    @PostMapping("/games")
    public ResponseEntity<MovieGenreModel> saveGameGenre(
            @RequestBody MovieGenreInput movieGenreInput) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gameGenreService.saveGenre(movieGenreInput));
    }

    @PutMapping("/games/{id}")
    public ResponseEntity<String> updateGameGenre(@PathVariable("id") int id, @RequestBody MovieGenreInput movieGenreInput) {
        return gameGenreService.updateGenre(id, movieGenreInput);
    }
}
