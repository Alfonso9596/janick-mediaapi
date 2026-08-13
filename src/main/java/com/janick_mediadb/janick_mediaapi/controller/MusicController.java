package com.janick_mediadb.janick_mediaapi.controller;

import com.janick_mediadb.janick_mediaapi.input.MusicInput;
import com.janick_mediadb.janick_mediaapi.model.FileInfoModel;
import com.janick_mediadb.janick_mediaapi.model.MusicModel;
import com.janick_mediadb.janick_mediaapi.model.RatingUpdateModel;
import com.janick_mediadb.janick_mediaapi.model.response.MusicResponse;
import com.janick_mediadb.janick_mediaapi.model.response.MusicSearchCriteria;
import com.janick_mediadb.janick_mediaapi.service.MusicService;
import com.janick_mediadb.janick_mediaapi.utils.AppConstants;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
