package com.janick_mediadb.janick_mediaapi.controller;

import com.janick_mediadb.janick_mediaapi.input.MovieGenreInput;
import com.janick_mediadb.janick_mediaapi.model.MovieGenreModel;
import com.janick_mediadb.janick_mediaapi.service.GameGenreService;
import com.janick_mediadb.janick_mediaapi.service.MovieGenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    @Autowired
    private MovieGenreService movieGenreService;

    @Autowired
    private GameGenreService gameGenreService;

    @GetMapping("/movies")
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

    @GetMapping("/games")
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
}
