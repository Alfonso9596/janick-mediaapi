package com.janick_mediadb.janick_mediaapi.controller;

import com.janick_mediadb.janick_mediaapi.exception.BadRequestException;
import com.janick_mediadb.janick_mediaapi.input.MovieInput;
import com.janick_mediadb.janick_mediaapi.model.FileInfoModel;
import com.janick_mediadb.janick_mediaapi.model.MovieModel;
import com.janick_mediadb.janick_mediaapi.model.RatingUpdateModel;
import com.janick_mediadb.janick_mediaapi.model.response.MovieResponse;
import com.janick_mediadb.janick_mediaapi.model.response.MovieSearchCriteria;
import com.janick_mediadb.janick_mediaapi.service.MovieService;
import com.janick_mediadb.janick_mediaapi.utils.AppConstants;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public MovieResponse getAllMovies(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE, required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @ParameterObject MovieSearchCriteria criteria) {
        return movieService.getAllMovies(page, pageSize, sortBy, sortDir, criteria);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieModel> getMovieById(
            @PathVariable("id") int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(movieService.getMovieById(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MovieModel> saveMovie(
            @RequestBody MovieInput movieInput) throws BadRequestException {

        MovieModel movieModel = movieService.saveMovie(movieInput);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(movieModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable int id) {
        return movieService.deleteMovie(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieModel> updateMovie(
            @PathVariable("id") int id,
            @RequestBody MovieInput movieInput) {

        MovieModel movieModel = movieService.updateMovie(id, movieInput);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(movieModel);
    }

    @PostMapping("/rating")
    public ResponseEntity<String> rateMovie(@RequestBody RatingUpdateModel ratingUpdateModel) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(movieService.rateMovie(ratingUpdateModel));
    }

    @GetMapping("/{id}/files")
    public ResponseEntity<List<FileInfoModel>> getMovieFiles(@PathVariable("id") int id) {
        return movieService.getMovieFiles(id);
    }
}
