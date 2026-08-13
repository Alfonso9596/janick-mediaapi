package com.janick_mediadb.janick_mediaapi.controller;

import com.janick_mediadb.janick_mediaapi.input.GenreInput;
import com.janick_mediadb.janick_mediaapi.model.GenreModel;
import com.janick_mediadb.janick_mediaapi.model.response.GenreResponse;
import com.janick_mediadb.janick_mediaapi.model.response.GenreSearchCriteria;
import com.janick_mediadb.janick_mediaapi.service.GameGenreService;
import com.janick_mediadb.janick_mediaapi.service.MovieGenreService;
import com.janick_mediadb.janick_mediaapi.service.MusicGenreService;
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

    private final MusicGenreService musicGenreService;

    @Autowired
    public GenreController(MovieGenreService movieGenreService, GameGenreService gameGenreService, MusicGenreService musicGenreService) {
        this.movieGenreService = movieGenreService;
        this.gameGenreService = gameGenreService;
        this.musicGenreService = musicGenreService;
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
    public ResponseEntity<List<GenreModel>> getAllMovieGenres() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(movieGenreService.getAllGenres());
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<GenreModel> getMovieGenreById(
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
    public ResponseEntity<GenreModel> saveMovieGenre(
            @RequestBody GenreInput genreInput) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(movieGenreService.saveGenre(genreInput));
    }

    @PutMapping("/movies/{id}")
    public ResponseEntity<String> updateMovieGenre(@PathVariable("id") int id, @RequestBody GenreInput genreInput) {
        return movieGenreService.updateGenre(id, genreInput);
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
    public ResponseEntity<List<GenreModel>> getAllGameGenres() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gameGenreService.getAllGenres());
    }

    @GetMapping("/games/{id}")
    public ResponseEntity<GenreModel> getGameGenreById(
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
    public ResponseEntity<GenreModel> saveGameGenre(
            @RequestBody GenreInput genreInput) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gameGenreService.saveGenre(genreInput));
    }

    @PutMapping("/games/{id}")
    public ResponseEntity<String> updateGameGenre(@PathVariable("id") int id, @RequestBody GenreInput genreInput) {
        return gameGenreService.updateGenre(id, genreInput);
    }

    @GetMapping("/music")
    public GenreResponse getPageableMusicGenres(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE, required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @ParameterObject GenreSearchCriteria criteria) {
        return musicGenreService.getPageableGenres(page, pageSize, sortBy, sortDir, criteria);
    }

    @GetMapping("/music/list")
    public ResponseEntity<List<GenreModel>> getAllMusicGenres() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(musicGenreService.getAllGenres());
    }

    @GetMapping("/music/{id}")
    public ResponseEntity<GenreModel> getMusicGenreById(
            @PathVariable("id") int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(musicGenreService.getGenreById(id));
    }

    @DeleteMapping("/music/{id}")
    public ResponseEntity<String> deleteMusicGenre(
            @PathVariable("id") int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(musicGenreService.deleteGenre(id));
    }

    @PostMapping("/music")
    public ResponseEntity<GenreModel> saveMusicGenre(
            @RequestBody GenreInput genreInput) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(musicGenreService.saveGenre(genreInput));
    }

    @PutMapping("/music/{id}")
    public ResponseEntity<String> updateMusicGenre(@PathVariable("id") int id, @RequestBody GenreInput genreInput) {
        return musicGenreService.updateGenre(id, genreInput);
    }
}
