package com.janickmediadb.janickmediaapi.controller;

import com.janickmediadb.janickmediaapi.input.MusicInput;
import com.janickmediadb.janickmediaapi.model.FileInfoModel;
import com.janickmediadb.janickmediaapi.model.MusicModel;
import com.janickmediadb.janickmediaapi.model.RatingUpdateModel;
import com.janickmediadb.janickmediaapi.model.response.MusicResponse;
import com.janickmediadb.janickmediaapi.model.response.MusicSearchCriteria;
import com.janickmediadb.janickmediaapi.service.MusicService;
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
@RequestMapping("/api/music")
public class MusicController {

    private final MusicService musicService;

    @Autowired
    public MusicController(MusicService musicService) {
        this.musicService = musicService;
    }

    @GetMapping
    public MusicResponse getAllMusic(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE, required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @ParameterObject MusicSearchCriteria criteria) {
        return musicService.getAllMusic(page, pageSize, sortBy, sortDir, criteria);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MusicModel> getMusicById(
            @PathVariable("id") int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(musicService.getMusicById(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MusicModel> saveMusic(
            @RequestBody MusicInput musicInput) {

        MusicModel musicModel = musicService.saveMusic(musicInput);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(musicModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMusic(@PathVariable("id") int id) {
        return musicService.deleteMusic(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MusicModel> updateMusic(
            @PathVariable("id") int id,
            @RequestBody MusicInput musicInput) {

        MusicModel musicModel = musicService.updateMusic(id, musicInput);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(musicModel);
    }

    @PostMapping("/rating")
    public ResponseEntity<String> rateMusic(
            @RequestBody RatingUpdateModel ratingUpdateModel) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(musicService.rateMusic(ratingUpdateModel));
    }

    @GetMapping("/{id}/files")
    public ResponseEntity<List<FileInfoModel>> getMusicFiles(
            @PathVariable("id") int id) {
        return musicService.getMusicFiles(id);
    }
}
