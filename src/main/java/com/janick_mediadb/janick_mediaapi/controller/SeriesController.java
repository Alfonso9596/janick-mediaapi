package com.janick_mediadb.janick_mediaapi.controller;

import com.janick_mediadb.janick_mediaapi.input.SeriesInput;
import com.janick_mediadb.janick_mediaapi.model.SeriesModel;
import com.janick_mediadb.janick_mediaapi.model.response.SeriesResponse;
import com.janick_mediadb.janick_mediaapi.model.response.SeriesSearchCriteria;
import com.janick_mediadb.janick_mediaapi.service.SeriesService;
import com.janick_mediadb.janick_mediaapi.utils.AppConstants;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/series")
public class SeriesController {

    @Autowired
    private SeriesService seriesService;

    @GetMapping
    public SeriesResponse getAllSeries(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE, required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @ParameterObject SeriesSearchCriteria criteria) {
        return seriesService.getAllSeries(page, pageSize, sortBy, sortDir, criteria);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeriesModel> getSeriesById(
            @PathVariable("id") int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(seriesService.getSeriesById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSeries(
            @PathVariable("id") int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(seriesService.deleteSeries(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SeriesModel> saveSeries(
            @RequestBody SeriesInput seriesInput) {

        SeriesModel seriesModel = seriesService.saveSeries(seriesInput);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(seriesModel);
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<String> rateMovie(
            @PathVariable("id") int id,
            @RequestParam("rating") int rating) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(seriesService.rateSeries(id, rating));
    }

}
