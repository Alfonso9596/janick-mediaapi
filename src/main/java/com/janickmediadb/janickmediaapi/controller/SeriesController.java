package com.janickmediadb.janickmediaapi.controller;

import com.janickmediadb.janickmediaapi.input.SeriesInput;
import com.janickmediadb.janickmediaapi.model.FileInfoModel;
import com.janickmediadb.janickmediaapi.model.RatingUpdateModel;
import com.janickmediadb.janickmediaapi.model.SeriesModel;
import com.janickmediadb.janickmediaapi.model.response.SeriesResponse;
import com.janickmediadb.janickmediaapi.model.response.SeriesSearchCriteria;
import com.janickmediadb.janickmediaapi.service.SeriesService;
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
@RequestMapping("/api/series")
public class SeriesController {

    private final SeriesService seriesService;

    @Autowired
    public SeriesController(SeriesService seriesService) {
        this.seriesService = seriesService;
    }

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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SeriesModel> saveSeries(
            @RequestBody SeriesInput seriesInput) {

        SeriesModel seriesModel = seriesService.saveSeries(seriesInput);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(seriesModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSeries(@PathVariable("id") int id) {
        return seriesService.deleteSeries(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeriesModel> updateSeries(
            @PathVariable("id") int id,
            @RequestBody SeriesInput seriesInput) {

        SeriesModel seriesModel = seriesService.updateSeries(id, seriesInput);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(seriesModel);
    }

    @PostMapping("/rating")
    public ResponseEntity<String> rateMovie(@RequestBody RatingUpdateModel ratingUpdateModel) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(seriesService.rateSeries(ratingUpdateModel));
    }

    @GetMapping("/{id}/files")
    public ResponseEntity<List<FileInfoModel>> getSeriesFiles(@PathVariable("id") int id) {
        return seriesService.getSeriesFiles(id);
    }

}
