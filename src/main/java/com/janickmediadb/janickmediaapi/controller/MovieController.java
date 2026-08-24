package com.janickmediadb.janickmediaapi.controller;

import com.janickmediadb.janickmediaapi.exception.BadRequestException;
import com.janickmediadb.janickmediaapi.input.MovieInput;
import com.janickmediadb.janickmediaapi.model.FileInfoModel;
import com.janickmediadb.janickmediaapi.model.MovieModel;
import com.janickmediadb.janickmediaapi.model.RatingUpdateModel;
import com.janickmediadb.janickmediaapi.model.response.MovieResponse;
import com.janickmediadb.janickmediaapi.model.response.MovieSearchCriteria;
import com.janickmediadb.janickmediaapi.service.MovieService;
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
